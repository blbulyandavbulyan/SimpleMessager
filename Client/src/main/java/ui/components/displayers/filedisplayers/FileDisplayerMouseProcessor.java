package ui.components.displayers.filedisplayers;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FileDisplayerMouseProcessor extends MouseAdapter {
    private Runnable removeFromDragAndDropPanelAction;
    private Runnable highlightAction;
    private Runnable showDetailsWindowAction;
    private JPopupMenu rightClickContextMenu;
    public FileDisplayerMouseProcessor(){
        rightClickContextMenu = new JPopupMenu();
        //todo insert here text from properties for menu item
        JMenuItem removeFileMenuItem = new JMenuItem("Удалить");
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
            //todo add showing context menu here
            if(rightClickContextMenu != null)
                rightClickContextMenu.show(e.getComponent(), e.getX(), e.getY());
        }
        super.mouseClicked(e);
    }

    public Runnable getRemoveFromDragAndDropPanelAction() {
        return removeFromDragAndDropPanelAction;
    }

    public Runnable getHighlightAction() {
        return highlightAction;
    }

    public Runnable getShowDetailsWindowAction() {
        return showDetailsWindowAction;
    }

    public JPopupMenu getRightClickContextMenu() {
        return rightClickContextMenu;
    }

    public void setRemoveFromDragAndDropPanelAction(Runnable removeFromDragAndDropPanelAction) {
        this.removeFromDragAndDropPanelAction = removeFromDragAndDropPanelAction;
    }

    public void setHighlightAction(Runnable highlightAction) {
        this.highlightAction = highlightAction;
    }

    public void setShowDetailsWindowAction(Runnable showDetailsWindowAction) {
        this.showDetailsWindowAction = showDetailsWindowAction;
    }

    public void setRightClickContextMenu(JPopupMenu rightClickContextMenu) {
        this.rightClickContextMenu = rightClickContextMenu;
    }
}
