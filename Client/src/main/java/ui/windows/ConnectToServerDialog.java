package ui.windows;

import ui.customuicomponents.textfieldswithghosttext.JTextFiledWithGhostText;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetSocketAddress;
import java.util.ResourceBundle;

import static ui.common.DisplayErrors.showErrorMessage;

public class ConnectToServerDialog extends JDialog {
    private JPanel contentPane;
    private JButton connectBtn;
    private JButton cancelButton;
    private JTextField serverAddressField;
    private JTextField serverPortField;
    private InetSocketAddress address;
    private boolean cancelled = false;
    private final ResourceBundle rb;
    public ConnectToServerDialog(ResourceBundle rb) {
        this.rb = rb;
        createUIComponents();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(connectBtn);

        connectBtn.addActionListener(e -> onOK());

        cancelButton.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.setMinimumSize(new Dimension(523, 144));
        this.setSize(this.getMinimumSize());
        this.setTitle(rb.getString("connectToServerDialog.windowCaption"));
    }

    private void onOK() {
        // add your code here
        try {
            String serverAddress = serverAddressField.getText().trim();
            if (serverAddress.isBlank()) {
                showErrorMessage(this, "connectToServerDialog.errorMessage.addressFieldIsEmpty", "connectToServerDialog.errorCaptions.connectionError", rb);
                return;
            }
            int serverPort = Integer.parseInt(serverPortField.getText().trim());
            address = new InetSocketAddress(serverAddress, serverPort);
            dispose();
        } catch (NumberFormatException e) {
            showErrorMessage(this, "connectToServerDialog.errorMessage.portIsNotANumber", "connectToServerDialog.errorCaptions.connectionError", rb);

        } catch (IllegalArgumentException e) {
            showErrorMessage(this, "connectToServerDialog.errorMessage.invalidPortOrInvalidAddress", "connectToServerDialog.errorCaptions.connectionError", rb);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        this.cancelled = true;
        dispose();
    }
    private void createUIComponents(){
        contentPane = new JPanel();
        connectBtn = new JButton(rb.getString("connectToServerDialog.connectButton"));
        cancelButton = new JButton(rb.getString("connectToServerDialog.cancelButton"));
        serverAddressField = new JTextFiledWithGhostText(rb.getString("connectToServerDialog.serverAddressFieldGhostText"));
        serverPortField = new JTextFiledWithGhostText(rb.getString("connectToServerDialog.serverPortFieldGhostText"));
        contentPane.setLayout(new GridLayout(5, 1));
        JPanel connectAndCancelButtonsPanel = new JPanel();
        connectAndCancelButtonsPanel.setLayout(new GridLayout(1, 3));
        connectAndCancelButtonsPanel.add(connectBtn);
        connectAndCancelButtonsPanel.add(Box.createHorizontalGlue());
        connectAndCancelButtonsPanel.add(cancelButton);

        contentPane.add(serverAddressField);
        contentPane.add(Box.createVerticalGlue());
        contentPane.add(serverPortField);
        contentPane.add(Box.createVerticalGlue());
        contentPane.add(connectAndCancelButtonsPanel);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    }
    public InetSocketAddress getAddress() {
        return address;
    }

    public boolean isCancelled() {
        return cancelled;
    }

//    public static void main(String[] args) {
//        ConnectToServerDialog dialog = new ConnectToServerDialog(ResourceBundle.getBundle("resources/locales/guitext"));
//        dialog.pack();
//        dialog.setVisible(true);
//        InetSocketAddress addr = dialog.getAddress();
//        System.out.printf("адресс: %s\nпорт: %d", addr.getHostName(), addr.getPort());
//        System.exit(0);
//    }

}
