package org.blbulyandavbulyan.client.ui.components.custom.passwordfieldwithshowpasscheckbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class ShowUnshowPasswordPanel extends JPanel {
    private final JPasswordField passwordField;
    private final ShowUnshowCheckBox showPassword;
    private char echoChar;

    public ShowUnshowPasswordPanel(){
        this(new JPasswordField());
    }
    public ShowUnshowPasswordPanel(JPasswordField jPasswordField){
        passwordField = jPasswordField;
        showPassword = new ShowUnshowCheckBox();
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        //gbc.insets = new Insets(3, 3, 3, 3);
        this.add(passwordField, gbc);
        //gbc.insets = new Insets(0, 3, 3,3);
        gbc.weighty = 1;
        gbc.weightx = 0.005;
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        this.add(showPassword, gbc);
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
