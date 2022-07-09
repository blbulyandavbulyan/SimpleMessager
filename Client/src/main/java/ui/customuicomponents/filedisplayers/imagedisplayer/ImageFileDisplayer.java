package ui.customuicomponents.filedisplayers.imagedisplayer;

import ui.customuicomponents.filedisplayers.FileDisplayer;

import javax.swing.*;
import java.io.File;

public class ImageFileDisplayer extends FileDisplayer {
    private ImageArea miniatureArea;
    private DisplayImageWindow displayImageWindow;
    public ImageFileDisplayer(File f) {
        super(f);
        ImageIcon imageIcon = new ImageIcon(f.getPath());
        displayImageWindow = new DisplayImageWindow(imageIcon);
        displayImageWindow.setTitle(f.getName());

    }
}
