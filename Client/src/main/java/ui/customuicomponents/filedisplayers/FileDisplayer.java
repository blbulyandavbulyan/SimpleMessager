package ui.customuicomponents.filedisplayers;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public abstract class FileDisplayer extends JPanel {
    protected final File displayedFile;
    protected FileDisplayer(File f){
        displayedFile = f;
        this.setLayout(new FlowLayout());
    }
}
