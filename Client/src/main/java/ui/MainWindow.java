package ui;

import general.Message;
import serverconnection.ServerConnection;
import ui.closedjtabbedpane.ClosableJTabedPane;
import common.MessagePrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
public class MainWindow extends JFrame implements MessagePrinter {
    private JPanel panel1;
    private JTextField messageField;
    private JButton sendBtn;
    private JTabbedPane dialogsTappedPane;
    private JTextArea generalDialogArea;
    private HashMap<String, JTextArea> privateDialogs = new HashMap<>();
    private ServerConnection connection;
    private String myUserName;
    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.pack();
        mw.setSize(new Dimension(500, 500));
        mw.setVisible(true);
        Message msg1 = new Message("Hello, i am johan", "johan", "you"),
        msg2 = new Message("Hello, i am georgy", "georgy", "you");
        for(int i = 0; i < 1000; i++){
            mw.printMessage(msg1);
            mw.printMessage(msg2);
        }
    }
    private void init(){
        ActionListener sendMessage = (e)->{
            String message = messageField.getText().trim();
            if(!message.isBlank()){
                String receiverUserName = null;
                int selectedDialogIndex = dialogsTappedPane.getSelectedIndex();
                if(selectedDialogIndex > 0){
                    receiverUserName = dialogsTappedPane.getTitleAt(selectedDialogIndex);
                }
                Message msg = new Message(message, myUserName, receiverUserName);
                try {
                    connection.sendMessage(msg);
                    printMessage(msg);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            (connection.isClosed() ? "Произошла ошибка отправки сообщения, соединение было закрыто." : "Произошла неизвестная ошибка при отправке сообщения."),
                            "Ошибка отправки сообщения.", JOptionPane.ERROR_MESSAGE);
                    if(connection.isClosed()){

                    }
                }

            }

        };
        messageField.addActionListener(sendMessage);
        sendBtn.addActionListener(sendMessage);
        getContentPane().add(panel1);
    }
    public MainWindow(ServerConnection connection){
        if(connection == null)throw new NullPointerException("connection is null");
        if(connection.isClosed())throw new RuntimeException("connection is closed");
        this.connection = connection;
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if(connection.isOpen()) {
                    try {
                        connection.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                super.windowClosed(e);
            }
        });
        init();
    }
    private MainWindow() {
        init();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public void printMessage(Message msg){
        if(msg.getReceiver() != null){
            String sender = msg.getSender();
            initPrivateDialogWithUser(sender);
            privateDialogs.get(sender).append(msg.toString()+"\n");
        }
        else{
            generalDialogArea.append(msg.toString() + "\n");
        }
    }
    private int initPrivateDialogWithUser(String username){
        if(!privateDialogs.containsKey(username)){
                JTextArea jTextArea = new JTextArea();
                jTextArea.setEditable(false);
                privateDialogs.put(username, jTextArea);
                ClosableJTabedPane.addTab(dialogsTappedPane, username, new JScrollPane(jTextArea));
        }
        return 0;
    }
}
