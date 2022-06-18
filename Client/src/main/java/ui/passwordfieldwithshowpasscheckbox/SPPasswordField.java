package ui.passwordfieldwithshowpasscheckbox;

import ui.ghosttextt.GhostText;
import ui.ghosttextt.SpecificShowUnshowGhostTextActions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class SPPasswordField  extends JPanel {
    private final JPasswordField passwordField;
    private final ShowUnshowCheckBox showPassword;
    private GhostText ghostText;
    private char echoChar;

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setLayout(new BorderLayout());
        JPanel passwordField = new SPPasswordField();
        jFrame.add(passwordField);
        jFrame.setPreferredSize(new Dimension(500, 200));
        jFrame.pack();
        jFrame.setVisible(true);
    }
    public SPPasswordField(){
        this.setLayout(new BorderLayout());
        passwordField = new JPasswordField();
        showPassword = new ShowUnshowCheckBox();
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.weightx = 1;
//        gbc.weighty = 1;
//        gbc.gridwidth = 1;
//        gbc.gridheight = 1;
//        gbc.fill = GridBagConstraints.BOTH;
//        this.add(passwordField, gbc);
//        gbc.weightx = 0.0001;
//        gbc.gridx = 1;
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
    public JPasswordField getPasswordField() {
        return passwordField;
    }
    public void setGhostText(String ghostText){
        if(this.ghostText != null)this.ghostText.delete();
        this.ghostText = new GhostText(passwordField, ghostText, new SpecificShowUnshowGhostTextActions() {
            @Override
            public void focusLost() {
                passwordField.setEchoChar((char) 0);
            }

            @Override
            public void focusGained() {
                passwordField.setEchoChar(showPassword.isSelected() ? (char)0 : echoChar );
            }
        });
        passwordField.setEchoChar((char)0);
    }
    public ShowUnshowCheckBox getShowPassword() {
        return showPassword;
    }
    public void setEchoChar(char echoChar){
        this.echoChar = echoChar;
        passwordField.setEchoChar(echoChar);
    }
    public void clear(){
        if (ghostText != null)ghostText.clear();
        else passwordField.setText("");
    }
}
