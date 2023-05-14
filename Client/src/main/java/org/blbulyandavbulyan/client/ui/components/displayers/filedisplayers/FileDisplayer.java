package org.blbulyandavbulyan.client.ui.components.displayers.filedisplayers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public abstract class FileDisplayer extends JPanel {
    protected boolean detailsWindowIsShow;
    protected boolean isFileSelected;
    protected final JFrame detailsWindow;
    protected final JLabel fileNameLabel;
    protected final File displayedFile;
//    protected final ResourceBundle rb;
    protected FileDisplayer(File f, JFrame detailsWindow){
//        this.rb = rb;
        fileNameLabel = new JLabel(f.getName());
        this.displayedFile = f;
        this.detailsWindow = detailsWindow;
        if(detailsWindow != null){
            detailsWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    detailsWindowIsShow = false;
                    super.windowClosing(e);
                }
            });
        }
        this.setLayout(new FlowLayout());
    }

    public abstract void selectOrDeselectFile();
    public void showOrHideDetailsWindow(){
        detailsWindow.setVisible(detailsWindowIsShow = !detailsWindowIsShow);
    }
    public void dispose(){
        if(detailsWindow != null)detailsWindow.dispose();
    }
    public boolean isDetailsWindowIsShow() {
        return detailsWindowIsShow;
    }

    public boolean isFileSelected() {
        return isFileSelected;
    }
    public void setPreferredHeight(int preferredHeight){
        //this.preferredHeight = preferredHeight;
    }

    public File getDisplayedFile() {
        return displayedFile;
    }
}
