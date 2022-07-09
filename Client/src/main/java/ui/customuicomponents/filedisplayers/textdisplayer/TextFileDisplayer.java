package ui.customuicomponents.filedisplayers.textdisplayer;

import ui.customuicomponents.filedisplayers.FileDisplayer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;

public class TextFileDisplayer extends FileDisplayer {
    DisplayTextWindow displayTextWindow;
    boolean windowDisplayed = false;
    protected final JLabel fileNameLabel;
    public TextFileDisplayer(File f) throws FileNotFoundException {
        super(f);
        fileNameLabel = new JLabel(f.getName());
        displayTextWindow = new DisplayTextWindow(f);
        fileNameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayTextWindow.setVisible(windowDisplayed = !windowDisplayed);
                super.mouseClicked(e);
            }
        });
        this.add(fileNameLabel);
    }
    public void dispose(){
        displayTextWindow.dispose();
    }

    public static void main(String[] args) throws FileNotFoundException {
        JFrame jFrame = new JFrame();
        jFrame.getContentPane().add(new TextFileDisplayer(new File("/home/david/smb.conf.back")));
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
