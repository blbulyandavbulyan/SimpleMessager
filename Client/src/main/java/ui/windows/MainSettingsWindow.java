package ui.windows;

import javax.swing.*;

public class MainSettingsWindow extends JDialog {
    public MainSettingsWindow(){


    }
    private JPanel createUiComponents(){
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        return contentPane;
    }
}
