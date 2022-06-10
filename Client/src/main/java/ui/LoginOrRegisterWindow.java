package ui;
import general.LoginOrRegisterRequest;
import userdata.LoginOrRegisterResultGetter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedList;

public class LoginOrRegisterWindow extends JDialog implements LoginOrRegisterResultGetter {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JPasswordField repeatPasswordField;
    private JPanel registerPanel;
    private JPanel loginPanel;
    private JButton clearPassword;
    private JButton clearUserName;
    private JButton clearRepeatPassword;
    private JButton submitBtn;
    private Label passwordLabel, userNameLabel, repeatPasswordLabel;
    private LoginOrRegisterRequest loginOrRegisterResult;
    private final Object notifyObject;

    public static void main(String[] args) {
        LoginOrRegisterWindow low = new LoginOrRegisterWindow();
        low.setVisible(true);
        while (true) {
            LoginOrRegisterRequest lor = low.getLoginOrRegisterResult();
            switch (lor.getOperation()) {
                case LOGIN -> {
                    System.out.println("Вы собрались входить.");
                    System.out.printf("Имя пользователя: %s\n", lor.getUserName());
                    System.out.printf("Пароль: %s\n", lor.getPassword());
                }
                case REGISTER -> {
                    System.out.println("Вы собрались регистрироваться.");
                    System.out.printf("Имя пользователя: %s\n", lor.getUserName());
                    System.out.printf("Пароль: %s\n", lor.getPassword());
                }
                case CANCELLED -> {
                    System.out.println("Операция отменена.");
                    return;
                }
            }
        }

    }

    private void initSubmitBtn() {
        ActionListener submitBtnPress = e -> {
            int selectedIndex = tabbedPane1.getSelectedIndex();
            String userName = userNameField.getText().trim();
            String password = String.valueOf(passwordField.getPassword()).trim();
            StringBuilder perhapsErrorMessage = new StringBuilder("Вы не заполнили: ");
            LinkedList<String> emptyFieldsNames = new LinkedList<>();
            if (userName.isBlank()) {
                emptyFieldsNames.add("имя пользователя");
            }
            if (password.isBlank()) {
                emptyFieldsNames.add("пароль");
            }
            if (emptyFieldsNames.isEmpty()) {
                try {
                    if (selectedIndex == 1) {
                        String repeatPassword = String.valueOf(repeatPasswordField.getPassword()).trim();
                        if (!repeatPassword.equals(password)) {
                            JOptionPane.showMessageDialog(((JButton) e.getSource()).getParent(), "Пароли не совпадают.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    loginOrRegisterResult = new LoginOrRegisterRequest(userName, password, selectedIndex == 0 ? LoginOrRegisterRequest.OperationType.LOGIN : LoginOrRegisterRequest.OperationType.REGISTER);
                    synchronized (notifyObject) {
                        notifyObject.notify();
                    }
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(((JButton) e.getSource()).getParent(), ex.getMessage(), "Ошибка, некоторые данные null", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                StringBuilder errorBuilder = new StringBuilder("Вы не заполнили: \n");
                for (String emptyFieldName : emptyFieldsNames) {
                    errorBuilder.append("\t");
                    errorBuilder.append(emptyFieldName);
                    errorBuilder.append(",\n");
                }
                errorBuilder.setLength(errorBuilder.length() - 2);
                JOptionPane.showMessageDialog(((JButton) e.getSource()).getParent(), errorBuilder.toString(), "Ошибка, вы заполнили не все поля", JOptionPane.ERROR_MESSAGE);
            }


        };
        submitBtn.addActionListener(submitBtnPress);
        panel1.registerKeyboardAction(submitBtnPress, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initClearBtns() {
        clearPassword.addActionListener(e -> passwordField.setText(""));
        clearUserName.addActionListener(e -> userNameField.setText(""));
        clearRepeatPassword.addActionListener(e -> repeatPasswordField.setText(""));
    }

    private void initTabPane() {
        tabbedPane1.addChangeListener(e -> {
            int selectedIndex = tabbedPane1.getSelectedIndex();
            if (selectedIndex == 0) {
                loginPanel.removeAll();
                registerPanel.removeAll();
                loginPanel.add(userNameLabel);
                loginPanel.add(userNameField);
                loginPanel.add(clearUserName);
                loginPanel.add(passwordLabel);
                loginPanel.add(passwordField);
                loginPanel.add(clearPassword);
            } else {
                loginPanel.removeAll();
                registerPanel.removeAll();
                registerPanel.add(userNameLabel);
                registerPanel.add(userNameField);
                registerPanel.add(clearUserName);
                registerPanel.add(passwordLabel);
                registerPanel.add(passwordField);
                registerPanel.add(clearPassword);
                registerPanel.add(repeatPasswordLabel);
                registerPanel.add(repeatPasswordField);
                registerPanel.add(clearRepeatPassword);
            }
            submitBtn.setText(tabbedPane1.getTitleAt(selectedIndex));
        });
    }

    public LoginOrRegisterWindow() {
        this.notifyObject = new Object();
        this.getContentPane().add(panel1);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loginOrRegisterResult = null;
                synchronized (notifyObject) {
                    notifyObject.notify();
                }
                System.exit(0);
                super.windowClosing(e);
            }
        });
        initTabPane();
        initClearBtns();
        initSubmitBtn();
        this.setMinimumSize(new Dimension(531, 252));
        this.setSize(this.getMinimumSize());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        passwordLabel = new Label("Пароль");
        userNameLabel = new Label("Имя пользователя");
        repeatPasswordLabel = new Label("Повторите пароль");
        loginPanel = new JPanel();
        registerPanel = new JPanel();
        userNameField = new JTextField();
        passwordField = new JPasswordField();
        clearPassword = new JButton("Очистить");
        clearUserName = new JButton("Очистить");
        clearRepeatPassword = new JButton("Очистить");
        repeatPasswordField = new JPasswordField();
        {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setColumns(3);
            gridLayout.setRows(2);
            loginPanel.setLayout(gridLayout);
        }
        loginPanel.add(userNameLabel);
        loginPanel.add(userNameField);
        loginPanel.add(clearUserName);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(clearPassword);
        {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setColumns(3);
            gridLayout.setRows(3);
            registerPanel.setLayout(gridLayout);
        }

    }

    public LoginOrRegisterRequest getLoginOrRegisterResult() {
        synchronized (notifyObject) {
            this.setVisible(true);
            try {
                notifyObject.wait();
                this.setVisible(false);
                return loginOrRegisterResult;
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.dispose();
                return null;
            }
        }
    }

    @Override
    public void printStatusMessage(String statusMessage) {

    }

    @Override
    public LoginOrRegisterRequest getLoginOrRegisterResult(ActionCode actionCode) {
        switch (actionCode) {
            case GET_NEW_USERNAME_BECAUSE_OLD_IS_ALREADY_REGISTERED -> JOptionPane.showMessageDialog(this, "Пользователь с таким именим уже существует, введите другое.", "Ошибка регистрации", JOptionPane.ERROR_MESSAGE);
            case GET_BECAUSE_INVALID_LOGIN_OR_PASSWORD -> JOptionPane.showMessageDialog(this, "Неверный логин или пароль", "Ошибка входа", JOptionPane.ERROR_MESSAGE);
        }
        return getLoginOrRegisterResult();
    }

    @Override
    public void close() {
        this.dispose();
    }

}
