package ui.components.displayers.filedisplayers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public abstract class FileDisplayer extends JPanel {
    protected boolean detailsWindowIsShow;
    protected final JFrame detailsWindow;
    protected final JLabel fileNameLabel;
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
    }
    public void dispose(){
        if(detailsWindow != null)detailsWindow.dispose();
    }
}
