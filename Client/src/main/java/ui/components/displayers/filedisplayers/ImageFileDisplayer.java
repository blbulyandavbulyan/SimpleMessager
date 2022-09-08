package ui.components.displayers.filedisplayers;

import ui.components.displayers.general.viewwindows.ViewImageWindow;
import ui.components.displayers.general.areas.MiniatureImageArea;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ResourceBundle;

public class ImageFileDisplayer extends FileDisplayer {
    private final MiniatureImageArea miniatureArea;
    private final LineBorder highlightBorder;
    public ImageFileDisplayer(File f) {
        super(f, new ViewImageWindow(f));
        ImageIcon imageIcon = ((ViewImageWindow)this.detailsWindow).getImageIcon();
        miniatureArea = new MiniatureImageArea(imageIcon, 50, false);
        miniatureArea.addMouseListener(fileDisplayerMouseProcessor);
        this.add(miniatureArea);
        highlightBorder = new LineBorder(Color.BLUE, 5);
    }

    @Override
    public void setPreferredHeight(int preferredHeight) {
        super.setPreferredHeight(preferredHeight);
        miniatureArea.setPreferredHeight(preferredHeight);
    }

    @Override
    protected void selectOrDeselectFile() {
        isFileSelected = !isFileSelected;
        if(isFileSelected)miniatureArea.setBorder(highlightBorder);
        else miniatureArea.setBorder(null);
    }
}
