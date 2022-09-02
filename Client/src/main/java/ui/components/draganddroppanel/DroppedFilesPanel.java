package ui.components.draganddroppanel;

import ui.components.displayers.filedisplayers.FileDisplayer;

import javax.swing.*;
import java.awt.*;

public class DroppedFilesPanel extends JPanel {
    public static void main(String[] args) {
        JButton[] buttons = new JButton[10];
        JPanel jPanel = new JPanel(new FlowLayout());
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton("Button" + i);
            jPanel.add(buttons[i]);
        }
        JFrame jFrame = new JFrame("TestedFlowLayoutWindow");
        jFrame.getContentPane().add(jPanel);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

    }
    public void add(FileDisplayer fileDisplayer){

    }
}
