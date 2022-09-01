package ui.components.displayers.filedisplayers;

import ui.components.displayers.general.viewwindows.ViewTextWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;

public class TextFileDisplayer extends FileDisplayer {
    public TextFileDisplayer(File f) throws FileNotFoundException {
        super(f, new ViewTextWindow(f));
        fileNameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                detailsWindow.setVisible(detailsWindowIsShow = !detailsWindowIsShow);
                super.mouseClicked(e);
            }
        });
        this.add(fileNameLabel);
    }
    public static void main(String[] args) throws FileNotFoundException {
        JFrame jFrame = new JFrame();
        jFrame.getContentPane().add(new TextFileDisplayer(new File("/home/david/smb.conf.back")));
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    @Override
    protected void selectOrDeselectFile() {
        isFileSelected = !isFileSelected;
        if(isFileSelected)fileNameLabel.setForeground(Color.BLUE);
        else fileNameLabel.setForeground(Color.BLUE);
    }
}
