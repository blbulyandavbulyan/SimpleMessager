package org.blbulyandavbulyan.smclient.ui.windows.main;

import org.blbulyandavbulyan.smclient.ui.components.custom.passwordfieldwithshowpasscheckbox.ShowUnshowPasswordPanel;
import org.blbulyandavbulyan.smclient.ui.components.custom.textfieldswithghosttext.JPasswordFieldWithGhostText;

import javax.swing.*;
import java.awt.*;

public class MainSettingsWindow extends JFrame {
    private final MainSettingsWindow me;
    public MainSettingsWindow(){
        me = this;
        this.getContentPane().add(createUiComponents());
        this.pack();
    }
    private JPanel createUiComponents(){
        JPanel contentPane = new JPanel();
        JTabbedPane settingsTabPane = new JTabbedPane();
        JPanel accountSettingsPanel = new JPanel(new GridLayout(3, 1));
        JLabel changePasswordLabel = new JLabel("Сменить пароль");
        // TODO: 03.09.2022 Fix incorrect scaling show password button
        JPasswordField oldPasswordField = new JPasswordFieldWithGhostText("Старый пароль");
        JPasswordField newPasswordField = new JPasswordFieldWithGhostText("Новый пароль");
        JPasswordField repeatNewPasswordField = new JPasswordFieldWithGhostText("Повторите новый пароль");
        accountSettingsPanel.add(new ShowUnshowPasswordPanel(oldPasswordField));
        accountSettingsPanel.add(new ShowUnshowPasswordPanel(newPasswordField));
        accountSettingsPanel.add(new ShowUnshowPasswordPanel(repeatNewPasswordField));
        settingsTabPane.addTab("Настройки аккаунта", accountSettingsPanel);

        JButton saveButton = new JButton("Сохранить");
        JButton cancelButton = new JButton("Отменить");
        saveButton.addActionListener(e -> {
            //todo add saving settings here

            me.dispose();
        });
        cancelButton.addActionListener(e -> me.dispose());

        JPanel cancelAndSaveButtonsPanel = new JPanel(new FlowLayout());
        cancelAndSaveButtonsPanel.add(saveButton);
        cancelAndSaveButtonsPanel.add(cancelButton);
        contentPane.setLayout(new BorderLayout());

        contentPane.add(settingsTabPane, BorderLayout.CENTER);
        contentPane.add(cancelAndSaveButtonsPanel, BorderLayout.SOUTH);
        return contentPane;
    }

    public static void main(String[] args) {
        MainSettingsWindow mainSettingsWindow = new MainSettingsWindow();
        mainSettingsWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainSettingsWindow.setVisible(true);
    }
}
