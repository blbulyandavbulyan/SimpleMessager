package org.blbulyandavbulyan.smclient.ui.components.displayers.general.viewwindows;

import org.blbulyandavbulyan.smclient.ui.components.displayers.general.areas.ImageArea;

import javax.swing.*;
import java.io.File;

public class ViewImageWindow extends ScrollableViewWindow {
    private ImageIcon imageIcon;
    public ViewImageWindow(File imageFile){
        this(new ImageIcon(imageFile.getPath()));
        this.setTitle(imageFile.getName());
    }
    public ViewImageWindow(ImageIcon imageIcon){
        this.imageIcon = imageIcon;
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        mainScrollPane.setViewportView(new ImageArea(imageIcon));
        this.pack();
    }
    public ImageIcon getImageIcon(){
        return imageIcon;
    }
    public static void main(String[] args) {
        ViewImageWindow viewImageWindow = new ViewImageWindow(new File("/media/share/ds1/Фото и Видео/Фотографии/20211110_111938.jpg"));
        viewImageWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        viewImageWindow.setVisible(true);
    }
}
