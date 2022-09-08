package ui.components.draganddroppanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

public class FileDisplayersMouseProcessor extends MouseAdapter {
    private Runnable removeFromDragAndDropPanelAction;
    private Runnable highlightAction;
    private Runnable showDetailsWindowAction;
    private final JPopupMenu rightClickContextMenu;
    public FileDisplayersMouseProcessor(ResourceBundle rb){
        JMenuItem removeFileMenuItem = new JMenuItem(rb.getString("mainWindow.delete"));
        rightClickContextMenu = new JPopupMenu();
        removeFileMenuItem.addActionListener(e -> {
            if(removeFromDragAndDropPanelAction!=null)removeFromDragAndDropPanelAction.run();
        });
        rightClickContextMenu.add(removeFileMenuItem);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount()== 1 && SwingUtilities.isLeftMouseButton(e) && highlightAction != null)highlightAction.run();
        else if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && showDetailsWindowAction != null)showDetailsWindowAction.run();
        else if(SwingUtilities.isMiddleMouseButton(e) && removeFromDragAndDropPanelAction != null)removeFromDragAndDropPanelAction.run();
        else if(e.getClickCount() == 1 && SwingUtilities.isRightMouseButton(e)){
            if(rightClickContextMenu != null)
                rightClickContextMenu.show(e.getComponent(), e.getX(), e.getY());
        }
        super.mouseClicked(e);
    }

}
