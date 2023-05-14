package org.blbulyandavbulyan.smclient.ui.components.draganddroppanel;

import org.blbulyandavbulyan.smclient.ui.components.displayers.filedisplayers.FileDisplayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

public class FileDisplayersMouseProcessor extends MouseAdapter {
    private final DroppedFileControllerInterface droppedFileControllerInterface;

    public FileDisplayersMouseProcessor(DroppedFileControllerInterface droppedFileControllerInterface, ResourceBundle rb){
        // FIXME: 14.05.2023 Убрать отсюда создание меню, или осмыслить его, т.к. тут оно ни к чему не привязано
        JMenuItem removeFileMenuItem = new JMenuItem(rb.getString("menu.delete"));
        JPopupMenu rightClickContextMenu = new JPopupMenu();
        rightClickContextMenu.add(removeFileMenuItem);
        this.droppedFileControllerInterface = droppedFileControllerInterface;

        removeFileMenuItem.addActionListener((l)->{

            for (FileDisplayer fileDisplayer : droppedFileControllerInterface.getFileDisplayers()) {
                if(fileDisplayer.isFileSelected())droppedFileControllerInterface.removeFileDisplayerFromDragAndDropPanel(fileDisplayer);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Component component = e.getComponent();
        if(component instanceof FileDisplayer fileDisplayer){
            if(e.getClickCount()== 1 && SwingUtilities.isLeftMouseButton(e))
                fileDisplayer.selectOrDeselectFile();
            else if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e))
                fileDisplayer.showOrHideDetailsWindow();
            else if(SwingUtilities.isMiddleMouseButton(e) && droppedFileControllerInterface != null)
                droppedFileControllerInterface.removeFileDisplayerFromDragAndDropPanel(fileDisplayer);
        }

    }

}
