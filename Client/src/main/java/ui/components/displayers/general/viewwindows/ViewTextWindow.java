package ui.components.displayers.general.viewwindows;

import javax.swing.*;
import java.io.*;

public class ViewTextWindow extends ScrollableViewWindow {
    public ViewTextWindow(File displayedFile) throws FileNotFoundException {
        this.setTitle(displayedFile.getName());
        JTextArea jTextArea = new JTextArea();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(displayedFile));
        bufferedReader.lines().forEach(
                str -> jTextArea.append(str + "\n")
        );
        jTextArea.setEditable(false);
        mainScrollPane.setViewportView(jTextArea);
        this.pack();
    }
    //public ViewTextWindow(byte[] )
    public static void main(String[] args) throws FileNotFoundException {
        ViewTextWindow displayTextWindow = new ViewTextWindow(new File("/home/david/smb.conf.back"));
        displayTextWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        displayTextWindow.setVisible(true);
    }
}
