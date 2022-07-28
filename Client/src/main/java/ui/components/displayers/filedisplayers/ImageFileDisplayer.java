package ui.components.displayers.filedisplayers;

import ui.components.displayers.general.viewwindows.ViewImageWindow;
import ui.components.displayers.general.areas.MiniatureImageArea;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class ImageFileDisplayer extends FileDisplayer {
    private MiniatureImageArea miniatureArea;
    public ImageFileDisplayer(File f) {
        super(f, new ViewImageWindow(f));
        ImageIcon imageIcon = ((ViewImageWindow)this.detailsWindow).getImageIcon();
        miniatureArea = new MiniatureImageArea(imageIcon, 50);
        miniatureArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                detailsWindow.setVisible(detailsWindowIsShow = !detailsWindowIsShow);
                super.mouseClicked(e);
            }
        });
        this.add(miniatureArea);
    }
}
