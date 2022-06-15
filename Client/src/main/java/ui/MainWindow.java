package ui;


import common.interfaces.MessageSender;
import general.message.Message;
import serverconnection.MessagesReaderThread;
import serverconnection.ServerConnection;
import common.interfaces.MessagePrinter;
import ui.closedjtabbedpane.JTabbedPaneWithCloseableTabs;
import ui.exceptions.PersonalMessageIsEmpty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;

public class MainWindow extends JFrame implements MessagePrinter {
    private JTextField messageField;
    private JButton sendBtn;
    private JTabbedPaneWithCloseableTabs dialogsTappedPane;
    private JTextArea generalDialogArea;
    private final HashMap<String, JTextArea> privateDialogs = new HashMap<>();
    private MessageSender messageSender;
    private String myUserName;
    private MessagesReaderThread readerThread;
    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.pack();
        mw.setSize(new Dimension(500, 500));
        mw.setVisible(true);
        Message msg1 = new Message("Hello, i am johan", "johan", "you"),
                msg2 = new Message("Hello, i am georgy", "georgy", "you");
        for (int i = 0; i < 1000; i++) {
            mw.printMessage(msg1);
            mw.printMessage(msg2);
        }
    }

    private static Message getMessageWithReceiverFromMsgStr(String msgStr, String senderName) throws PersonalMessageIsEmpty{
        String receiverName = null;
        String newMsgStr = msgStr;
        char[] msgChars = msgStr.toCharArray();
        if (msgChars[0] == '@') {
            StringBuilder receiverNameBuilder = new StringBuilder();
            int i = 1;
            for (; i < msgChars.length && (msgChars[i] != ',' && msgChars[i] != ' '); i++) {
                receiverNameBuilder.append(msgChars[i]);
            }
            receiverName = receiverNameBuilder.toString();
            i++;
            if(i >= msgChars.length ||
                    (newMsgStr = String.valueOf(msgChars, i, msgChars.length - i)).isBlank())
                throw new PersonalMessageIsEmpty("Вы пытаетесь отправить пустое личное сообщение, после упоминания пользователя обязательно должен быть текст сообщения.");
        }
        return new Message(newMsgStr, senderName, receiverName);
    }

    private void init() {
        messageField = new JTextField();
        sendBtn = new JButton("Отправить");

        ActionListener sendMessage = (e) -> {
            String message = messageField.getText().trim();
            if (!message.isBlank()) {
                int selectedDialogIndex = dialogsTappedPane.getSelectedIndex();
                try {
                    Message msg = null;
                    if (selectedDialogIndex > 0) {
                        String receiverUserName = dialogsTappedPane.getTitleAt(selectedDialogIndex);
                        msg = new Message(message, myUserName, receiverUserName);
                    } else if (selectedDialogIndex == 0)msg = getMessageWithReceiverFromMsgStr(message, myUserName);
                    if (msg != null) {
                        messageSender.sendMessage(msg);
                        printMessage(msg);
                        messageField.setText("");
                    } else
                        JOptionPane.showMessageDialog(((JComponent) e.getSource()).getParent(), "Ошибка отправки сообщения, по каким-то причинам оно null", "Ошибка отправки", JOptionPane.ERROR_MESSAGE);

                }
                catch (PersonalMessageIsEmpty ex){
                    JOptionPane.showMessageDialog(((JComponent) e.getSource()).getParent(), ex.getMessage(),
                            "Ошибка отправки сообщения.", JOptionPane.ERROR_MESSAGE);
                    messageField.requestFocus();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(((JComponent) e.getSource()).getParent(),
                            (messageSender.isClosed() ? "Произошла ошибка отправки сообщения, соединение было закрыто." : "Произошла неизвестная ошибка при отправке сообщения."),
                            "Ошибка отправки сообщения.", JOptionPane.ERROR_MESSAGE);
                    if (messageSender.isClosed()) {

                    }
                    System.exit(-1);
                }

            }

        };
        messageField.addActionListener(sendMessage);
        sendBtn.addActionListener(sendMessage);
        dialogsTappedPane = new JTabbedPaneWithCloseableTabs(privateDialogs::remove, "Закрыть диалог");
        generalDialogArea = new JTextArea();
        generalDialogArea.setEditable(false);
        dialogsTappedPane.addTab("Oбщий", new JScrollPane(generalDialogArea));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        this.add(dialogsTappedPane, gbc);
        gbc.insets = new Insets(0, 3, 3,3);
        gbc.weighty = 0.05;
        gbc.weightx = 0.99999;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        this.add(messageField, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.00001;
        this.add(sendBtn, gbc);
    }

    public MainWindow(ServerConnection connection) {
        if (connection == null) throw new NullPointerException("connection is null");
        if (connection.isClosed()) throw new RuntimeException("connection is closed");
        init();
        this.messageSender = connection;
        this.myUserName = connection.getUserName();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setTitle(myUserName);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (messageSender.isOpen()) {
                    try {
                        readerThread.terminate();
                        messageSender.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.exit(-1);
                    }
                }
                System.exit(0);
            }
        });
        this.setMinimumSize(new Dimension(662, 378));
        this.setSize(this.getMinimumSize());
        readerThread = new MessagesReaderThread(connection, this);
    }

    private MainWindow() {
        init();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void printMessage(Message msg) {
        if (msg.getReceiver() != null) {
            String dialogName = msg.getReceiver().equals(myUserName) ?  msg.getSender() : msg.getReceiver();
            initPrivateDialogWithUser(dialogName);
            privateDialogs.get(dialogName).append(msg + "\n");
        } else {
            generalDialogArea.append(msg + "\n");
        }
    }

    private int initPrivateDialogWithUser(String username) {
        synchronized (privateDialogs){
            if (!privateDialogs.containsKey(username)) {
                JTextArea jTextArea = new JTextArea();
                jTextArea.setEditable(false);
                privateDialogs.put(username, jTextArea);
                dialogsTappedPane.addCloseableTab(username, new JScrollPane(jTextArea));
            }
        }

        return 0;
    }
}

