package ui;
import general.LoginOrRegisterRequest;
import ui.ghosttexttooltip.GhostText;
import ui.passwordfieldwithshowpasscheckbox.SPPasswordField;
import userdata.LoginOrRegisterResultGetter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

public class LoginOrRegisterWindow extends JFrame implements LoginOrRegisterResultGetter {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JPasswordField repeatPasswordField;
    private JPanel registerPanel;
    private JPanel loginPanel;
    private JPanel userNamePanel, passwordPanel, repeatPasswordPanel;
    private JButton submitBtn;
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

    private void initFields(){
        userNameField.addActionListener((a)-> passwordField.requestFocus());
        passwordField.addActionListener((a)->{
            if(tabbedPane1.getSelectedIndex() == 0)submitBtn.requestFocus();
            else repeatPasswordField.requestFocus();
        });
        repeatPasswordField.addActionListener((a)-> submitBtn.requestFocus());
    }
    private void initTabPane() {
        tabbedPane1.addChangeListener(e -> {
            int selectedIndex = tabbedPane1.getSelectedIndex();
            if (selectedIndex == 0) {
                loginPanel.removeAll();
                registerPanel.removeAll();
                loginPanel.add(userNamePanel);
                loginPanel.add(passwordPanel);
            } else {
                loginPanel.removeAll();
                registerPanel.removeAll();
                registerPanel.add(userNamePanel);
                registerPanel.add(passwordPanel);
                registerPanel.add(repeatPasswordPanel);
            }
            submitBtn.setText(tabbedPane1.getTitleAt(selectedIndex));
        });
    }
    public void init(){
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
        initSubmitBtn();
        initFields();

    }
    public LoginOrRegisterWindow() {
        this.notifyObject = new Object();
        this.getContentPane().add(panel1);

        init();
        this.setMinimumSize(new Dimension(531, 252));
        this.setSize(this.getMinimumSize());
        //this.setResizable(false);
    }

    private void createUIComponents() {
        loginPanel = new JPanel();
        registerPanel = new JPanel();
        userNamePanel = new JPanel();
        passwordPanel = new JPanel();
        repeatPasswordPanel = new JPanel();
        userNameField = new JTextField();
        SPPasswordField passwordFieldPanel = new SPPasswordField();
        SPPasswordField repeatPasswordFieldPanel = new SPPasswordField();
        //
        //passwordFieldPanel.setPreferredSize(d);
        //userNameField.setPreferredSize(d);
        //repeatPasswordPa
        JButton clearPassword = new JButton("Очистить");
        JButton clearUserName = new JButton("Очистить");
        JButton clearRepeatPassword = new JButton("Очистить");

        passwordField = passwordFieldPanel.getPasswordField();
        repeatPasswordField = repeatPasswordFieldPanel.getPasswordField();
        GhostText usernameGhostText = new GhostText(userNameField, "Введите имя пользователя...");
        passwordFieldPanel.setGhostText("Введите пароль...");
        repeatPasswordFieldPanel.setGhostText("Повторите пароль...");
        clearUserName.addActionListener(e -> usernameGhostText.clear());
        clearPassword.addActionListener(e -> passwordFieldPanel.clear());
        clearRepeatPassword.addActionListener(e -> repeatPasswordFieldPanel.clear());
        {
            userNamePanel.setLayout(new GridLayout(1, 2));
           // userNamePanel.add(userNameLabel);
            userNamePanel.add(userNameField, BorderLayout.WEST);
            userNamePanel.add(clearUserName, BorderLayout.EAST);
            //userNamePanel.setBorder(new EmptyBorder(5, 20, 20, 20));
        }
        {
//            GridLayout gridLayout = new GridLayout();
//            gridLayout.setColumns(3);
//            gridLayout.setRows(1);
            passwordPanel.setLayout(new GridLayout(1, 2));
            //passwordPanel.add(passwordLabel);
            passwordPanel.add(passwordFieldPanel);
            passwordPanel.add(clearPassword);
            //passwordPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        }
        {
//            GridLayout gridLayout = new GridLayout();
//            gridLayout.setColumns(3);
//            gridLayout.setRows(1);
            repeatPasswordPanel.setLayout(new GridLayout(1, 2));
            //repeatPasswordPanel.add(repeatPasswordLabel);
            repeatPasswordPanel.add(repeatPasswordFieldPanel);
            repeatPasswordPanel.add(clearRepeatPassword);
           // repeatPasswordPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        }
        {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setColumns(1);
            gridLayout.setRows(2);
            loginPanel.setLayout(gridLayout);
        }
        loginPanel.add(userNamePanel);
        loginPanel.add(passwordPanel);
        {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setColumns(1);
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
