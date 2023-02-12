package ui.windows.connecting;
import general.loginorregisterrequest.LoginOrRegisterRequest;
import ui.components.custom.textfieldswithghosttext.JPasswordFieldWithGhostText;
import ui.components.custom.textfieldswithghosttext.JTextFiledWithGhostText;
import ui.components.custom.passwordfieldwithshowpasscheckbox.ShowUnshowPasswordPanel;
import userdata.LoginOrRegisterResultGetter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static ui.common.DisplayErrors.showErrorMessage;

public class LoginOrRegisterWindow extends JFrame implements LoginOrRegisterResultGetter {
    private JTabbedPane loginOrRegisterTabPane;
    private JTextFiledWithGhostText userNameField;
    private JPasswordField passwordField;
    private JPasswordField repeatPasswordField;
    private JPanel registerPanel;
    private JPanel loginPanel;
    private JPanel userNamePanel, passwordPanel, repeatPasswordPanel;
    private JButton submitBtn;
    private LoginOrRegisterRequest loginOrRegisterResult;
    private final Object notifyObject;
    private final ResourceBundle rb;

    public static void main(String[] args) {
        LoginOrRegisterWindow low = new LoginOrRegisterWindow(ResourceBundle.getBundle("resources/locales/guitext"));
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
            int selectedIndex = loginOrRegisterTabPane.getSelectedIndex();
            String userName = userNameField.getText().trim();
            char []passwordChars = passwordField.getPassword();
            String password = String.valueOf(passwordChars).trim();
            Arrays.fill(passwordChars, (char)0);

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
                        char []repeatPasswordChars = repeatPasswordField.getPassword();
                        String repeatPassword = String.valueOf(repeatPasswordChars).trim();
                        Arrays.fill(repeatPasswordChars, (char)0);
                        if (!repeatPassword.equals(password)) {
                            showErrorMessage(((JButton) e.getSource()).getParent(), "loginOrRegisterWindow.errorMessages.PasswordsIsNotEquals", "errorCaptions.generalErrorCaption", rb);
                            return;
                        }
                    }
                    loginOrRegisterResult = new LoginOrRegisterRequest(userName, password, selectedIndex == 0 ? LoginOrRegisterRequest.OperationType.LOGIN : LoginOrRegisterRequest.OperationType.REGISTER);
                    synchronized (notifyObject) {
                        notifyObject.notify();
                    }
                } catch (NullPointerException ex) {
                    showErrorMessage(((JButton) e.getSource()).getParent(), ex.getMessage(), "errorMessages.someDataIsNull", rb);
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
        //panel1.registerKeyboardAction(submitBtnPress, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initFields(){
        userNameField.addActionListener((a)-> passwordField.requestFocus());
        passwordField.addActionListener((a)->{
            if(loginOrRegisterTabPane.getSelectedIndex() == 0)submitBtn.requestFocus();
            else repeatPasswordField.requestFocus();
        });
        repeatPasswordField.addActionListener((a)-> submitBtn.requestFocus());
    }
    private void initTabPane() {
        loginOrRegisterTabPane.addChangeListener(e -> {
            int selectedIndex = loginOrRegisterTabPane.getSelectedIndex();
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
            submitBtn.setText(loginOrRegisterTabPane.getTitleAt(selectedIndex));
        });
    }
    public void init(){
        createUIComponents();
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
    public LoginOrRegisterWindow(ResourceBundle rb) {
        this.rb = rb;
        this.notifyObject = new Object();

        init();
        this.setMinimumSize(new Dimension(500, 200));
        this.setSize(this.getMinimumSize());
        //this.setResizable(false);
        this.setTitle(rb.getString("loginOrRegisterWindow.windowCaption"));
    }

    private void createUIComponents() {
        loginOrRegisterTabPane = new JTabbedPane();
        JPanel contentPanel = new JPanel();
        loginPanel = new JPanel();
        registerPanel = new JPanel();
        userNamePanel = new JPanel();
        passwordPanel = new JPanel();
        repeatPasswordPanel = new JPanel();
        userNameField = new JTextFiledWithGhostText(rb.getString("loginOrRegisterWindow.usernameFieldGhostText"));
        submitBtn = new JButton(rb.getString("loginOrRegisterWindow.loginTabAndSubmitButtonText"));
        passwordField = new JPasswordFieldWithGhostText(rb.getString("loginOrRegisterWindow.passwordFieldGhostText"));
        repeatPasswordField = new JPasswordFieldWithGhostText(rb.getString("loginOrRegisterWindow.repeatPasswordGhostText"));
        ShowUnshowPasswordPanel passwordFieldPanel = new ShowUnshowPasswordPanel(passwordField);
        ShowUnshowPasswordPanel repeatPasswordFieldPanel = new ShowUnshowPasswordPanel(repeatPasswordField);
        ((JPasswordFieldWithGhostText)passwordField).setIsShowGetter(passwordFieldPanel::isPasswordShow);
        ((JPasswordFieldWithGhostText)repeatPasswordField).setIsShowGetter(repeatPasswordFieldPanel::isPasswordShow);
        String clearButtonText = rb.getString("loginOrRegisterWindow.clearButton");
        JButton clearPassword = new JButton(clearButtonText);
        JButton clearUserName = new JButton(clearButtonText);
        JButton clearRepeatPassword = new JButton(clearButtonText);


        clearUserName.addActionListener(e -> userNameField.setText(""));
        clearPassword.addActionListener(e -> passwordField.setText(""));
        clearRepeatPassword.addActionListener(e -> repeatPasswordField.setText(""));
        EmptyBorder generalBorder = new EmptyBorder(2, 0, 2, 0);
        userNamePanel.setLayout(new BorderLayout());
        userNamePanel.add(userNameField, BorderLayout.CENTER);
        userNamePanel.add(clearUserName, BorderLayout.EAST);

        userNamePanel.setBorder(generalBorder);

        passwordPanel.setLayout(new BorderLayout());
        passwordPanel.add(passwordFieldPanel, BorderLayout.CENTER);
        passwordPanel.add(clearPassword, BorderLayout.EAST);
        passwordPanel.setBorder(generalBorder);

        repeatPasswordPanel.setLayout(new BorderLayout());
        repeatPasswordPanel.add(repeatPasswordFieldPanel, BorderLayout.CENTER);
        repeatPasswordPanel.add(clearRepeatPassword, BorderLayout.EAST);
        repeatPasswordPanel.setBorder(generalBorder);
        loginPanel.setLayout(new GridLayout(2, 1));
        registerPanel.setLayout(new GridLayout(3, 1));

        loginPanel.add(userNamePanel);
        loginPanel.add(passwordPanel);
        loginOrRegisterTabPane.addTab(rb.getString("loginOrRegisterWindow.loginTabAndSubmitButtonText"), loginPanel);
        loginOrRegisterTabPane.addTab(rb.getString("loginOrRegisterWindow.registerTabAndSubmitButtonText"), registerPanel);

        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(loginOrRegisterTabPane, BorderLayout.CENTER);
        contentPanel.add(submitBtn, BorderLayout.SOUTH);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.getContentPane().add(contentPanel);
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
            case GET_NEW_USERNAME_BECAUSE_OLD_IS_ALREADY_REGISTERED -> showErrorMessage(this, "loginOrRegisterWindow.errorMessages.userIsAlreadyExists", "loginOrRegisterWindow.errorCaptions.registrationError", rb);
            case GET_BECAUSE_INVALID_LOGIN_OR_PASSWORD -> showErrorMessage(this, "loginOrRegisterWindow.errorMessages.invalidLoginOrPassword", "loginOrRegisterWindow.errorCaptions.loginError", rb);
        }
        return getLoginOrRegisterResult();
    }

    @Override
    public void close() {
        this.dispose();
    }

}