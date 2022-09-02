package ui.components.displayers.filedisplayers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public abstract class FileDisplayer extends JPanel {
    protected boolean detailsWindowIsShow;
    protected boolean isFileSelected;
    protected final JFrame detailsWindow;
    protected final JLabel fileNameLabel;
    protected FileDisplayerMouseProcessor fileDisplayerMouseProcessor;
    protected FileDisplayer(File f, JFrame detailsWindow){
        fileNameLabel = new JLabel(f.getName());
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
        fileDisplayerMouseProcessor = new FileDisplayerMouseProcessor();
        fileDisplayerMouseProcessor.setShowDetailsWindowAction(this::showOrHideDetailsWindow);
        fileDisplayerMouseProcessor.setHighlightAction(this::selectOrDeselectFile);
    }
    public void setRemoveFromDragAndDropPanelAction(Runnable removeAction){
        fileDisplayerMouseProcessor.setRemoveFromDragAndDropPanelAction(removeAction);
    }
    protected void showOrHideDetailsWindow(){
        detailsWindow.setVisible(detailsWindowIsShow = !detailsWindowIsShow);
    }
    protected abstract void selectOrDeselectFile();
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
}
