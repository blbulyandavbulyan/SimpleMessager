package org.blbulyandavbulyan.smclient.ui.components.displayers.filedisplayers;

import org.blbulyandavbulyan.smclient.ui.components.displayers.general.viewwindows.ViewImageWindow;
import org.blbulyandavbulyan.smclient.ui.components.displayers.general.areas.MiniatureImageArea;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;

public class ImageFileDisplayer extends FileDisplayer {
    private final MiniatureImageArea miniatureArea;
    private final LineBorder highlightBorder;
    public ImageFileDisplayer(File f) {
        super(f, new ViewImageWindow(f));
        ImageIcon imageIcon = ((ViewImageWindow)this.detailsWindow).getImageIcon();
        miniatureArea = new MiniatureImageArea(imageIcon, 50, false);
        this.add(miniatureArea);
        highlightBorder = new LineBorder(Color.BLUE, 5);
    }

    @Override
    public void setPreferredHeight(int preferredHeight) {
        super.setPreferredHeight(preferredHeight);
        miniatureArea.setPreferredHeight(preferredHeight);
    }

    @Override
    public void selectOrDeselectFile() {
        isFileSelected = !isFileSelected;
        if(isFileSelected)miniatureArea.setBorder(highlightBorder);
        else miniatureArea.setBorder(null);
    }
}
