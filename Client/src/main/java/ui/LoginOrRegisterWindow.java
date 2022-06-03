package ui;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class LoginOrRegisterWindow extends JDialog{
    private JTabbedPane tabbedPane1;
    private JTextField serverIPField;
    private JTextField serverPortField;
    private JPanel panel1;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private  JPasswordField repeatPasswordField;
    private JPanel registerPanel;
    private JPanel loginPanel;
    private JButton clearPassword;
    private JButton clearUserName;
    private JButton clearRepeatPassword;
    private JButton submitBtn;
    private JButton clearIP;
    private JButton clearPort;
    private Label passwordLabel, userNameLabel, repeatPasswordLabel;
    public static void main(String[] args) {
        LoginOrRegisterWindow lor = new LoginOrRegisterWindow();
        lor.pack();
        lor.setSize(new Dimension(500, 500));
        lor.setVisible(true);
    }
    public LoginOrRegisterWindow(){

        this.getContentPane().add(panel1);
        tabbedPane1.addChangeListener(e -> {
            int selectedIndex = tabbedPane1.getSelectedIndex();
            if(selectedIndex == 0){
                loginPanel.removeAll();
                registerPanel.removeAll();
                loginPanel.add(userNameLabel);
                loginPanel.add(userNameField);
                loginPanel.add(clearUserName);
                loginPanel.add(passwordLabel);
                loginPanel.add(passwordField);
                loginPanel.add(clearPassword);
                submitBtn.setText("Войти");
            }
            else{
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
                submitBtn.setText("Зарегистрироваться");
            }
        });
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
}
