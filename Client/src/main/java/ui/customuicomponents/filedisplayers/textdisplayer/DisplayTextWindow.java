package ui.customuicomponents.filedisplayers.textdisplayer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.function.Consumer;

public class DisplayTextWindow extends JFrame {
    public DisplayTextWindow(File displayedFile) throws FileNotFoundException {
        this.setTitle(displayedFile.getName());
        this.getContentPane().add(createRootComponent(displayedFile));
        this.pack();
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    }
    private Component createRootComponent(File displayedFile) throws FileNotFoundException {
        JTextArea jTextArea = new JTextArea();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(displayedFile));
        bufferedReader.lines().forEach(
                str -> jTextArea.append(str + "\n")
        );
        jTextArea.setEditable(false);
        return new JScrollPane(jTextArea);
    }

    public static void main(String[] args) throws FileNotFoundException {
        DisplayTextWindow displayTextWindow = new DisplayTextWindow(new File("/home/david/smb.conf.back"));
        displayTextWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        displayTextWindow.setVisible(true);
    }
}
