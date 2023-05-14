package org.blbulyandavbulyan.smclient.ui.components.displayers.filedisplayers;

import org.blbulyandavbulyan.smclient.ui.components.displayers.general.viewwindows.ViewTextWindow;

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

    @Override
    public void selectOrDeselectFile() {
        isFileSelected = !isFileSelected;
        if(isFileSelected)fileNameLabel.setForeground(Color.BLUE);
        else fileNameLabel.setForeground(Color.BLUE);
    }
}
