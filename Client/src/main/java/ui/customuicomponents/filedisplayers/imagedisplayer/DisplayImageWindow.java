package ui.customuicomponents.filedisplayers.imagedisplayer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

public class DisplayImageWindow extends JFrame {
    public DisplayImageWindow(File imageFile){
        this(new ImageIcon(imageFile.getPath()));
        this.setTitle(imageFile.getName());
    }
    public DisplayImageWindow(ImageIcon imageIcon){
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.getContentPane().add(createRootPane(imageIcon));
        this.pack();
    }
    private Component createRootPane(File imageFile){
        return createRootPane(new ImageIcon(imageFile.getPath()));
    }
    private Component createRootPane(ImageIcon imageIcon){
        return new JScrollPane(new ImageArea(imageIcon));
    }
    public static void main(String[] args) {
        DisplayImageWindow displayImageWindow = new DisplayImageWindow(new File("/media/share/ds1/Фото и Видео/Фотографии/20211110_111938.jpg"));
        displayImageWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        displayImageWindow.setVisible(true);
    }
}
