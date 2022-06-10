package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetSocketAddress;

public class ConnectToServerDialog extends JDialog {
    private JPanel contentPane;
    private JButton connectBtn;
    private JButton buttonCancel;
    private JTextField serverAddressField;
    private JTextField serverPortField;
    private InetSocketAddress address;
    private boolean cancelled = false;

    public ConnectToServerDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(connectBtn);

        connectBtn.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

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
    }

    private void onOK() {
        // add your code here
        try {
            String serverAddress = serverAddressField.getText().trim();
            if (serverAddress.isBlank()) {
                JOptionPane.showMessageDialog(this, "Ошибка, поле адреса пусто", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int serverPort = Integer.parseInt(serverPortField.getText().trim());
            address = new InetSocketAddress(serverAddress, serverPort);
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Вы ввели в поле порта не число\n" + e.getMessage(), "Ошибка!", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Вы ввели или неправильный порт, или неправильный адрес\n" + e.getMessage(), "Ошибка!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        this.cancelled = true;
        dispose();
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public static void main(String[] args) {
        ConnectToServerDialog dialog = new ConnectToServerDialog();
        dialog.pack();
        dialog.setVisible(true);
        InetSocketAddress addr = dialog.getAddress();
        System.out.printf("адресс: %s\nпорт: %d", addr.getHostName(), addr.getPort());
        System.exit(0);
    }

}
