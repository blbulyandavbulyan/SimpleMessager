package ui.customuicomponents.passwordfieldwithshowpasscheckbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class ShowUnshowPasswordPanel extends JPanel {
    private final JPasswordField passwordField;
    private final ShowUnshowCheckBox showPassword;
    private final char echoChar;

    public ShowUnshowPasswordPanel(){
        this(new JPasswordField());
    }
    public ShowUnshowPasswordPanel(JPasswordField jPasswordField){
        passwordField = jPasswordField;
        this.setLayout(new BorderLayout());
        showPassword = new ShowUnshowCheckBox();
        this.add(passwordField, BorderLayout.CENTER);
        this.add(showPassword, BorderLayout.EAST);
        showPassword.setBackground(passwordField.getBackground());
        this.setBorder(passwordField.getBorder());
        passwordField.setBorder(null);
        echoChar = passwordField.getEchoChar();
        showPassword.addItemListener(e -> {
            switch (e.getStateChange()){
                case ItemEvent.SELECTED -> {
                    passwordField.setEchoChar((char) 0);
                    passwordField.requestFocus();
                }
                case ItemEvent.DESELECTED -> {
                    passwordField.setEchoChar(echoChar);
                    passwordField.requestFocus();
                }
            }
        });
    }

    public boolean isPasswordShow(){
        return showPassword != null && showPassword.isSelected();
    }
    public JPasswordField getPasswordField() {
        return passwordField;
    }
    public ShowUnshowCheckBox getShowPassword() {
        return showPassword;
    }
}
