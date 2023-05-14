package org.blbulyandavbulyan.smclient.ui.components.draganddroppanel;

import org.blbulyandavbulyan.smclient.ui.components.draganddroppanel.layouts.WrapLayout;
import org.blbulyandavbulyan.smclient.ui.components.displayers.filedisplayers.FileDisplayer;
import org.blbulyandavbulyan.smclient.ui.components.displayers.generators.FileDisplayerGenerator;
import org.blbulyandavbulyan.smclient.ui.components.draganddroppanel.interfaces.DroppedFilesListener;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.*;
import java.util.List;

public class DragAndDropPanel extends JPanel implements DroppedFileControllerInterface {
    private final CardLayout cardLayout = new CardLayout();
    private final Map<File, FileDisplayer> droppedFilesMap = new HashMap<>();
    private final JPanel droppedFilesPanel;
    private final DragAndDropPanel me;
    private static class DroppedFilesListenersProcessor implements DroppedFilesListener{
        private final Set<DroppedFilesListener> droppedFilesListeners = new HashSet<>();
        public void addListener(DroppedFilesListener droppedFilesListener){
            droppedFilesListeners.add(droppedFilesListener);
        }
        public boolean removeListener(DroppedFilesListener droppedFilesListener){
            return droppedFilesListeners.remove(droppedFilesListener);
        }
        public void removeAllListeners(){
            droppedFilesListeners.clear();
        }
        @Override
        public void droppedFileAdded(File file) {
            droppedFilesListeners.forEach(listener -> listener.droppedFileAdded(file));
        }

        @Override
        public void droppedFileDeleted(File file) {
            droppedFilesListeners.forEach(listener -> listener.droppedFileDeleted(file));
        }

        @Override
        public void droppedFilesSetCleared() {
            droppedFilesListeners.forEach(DroppedFilesListener::droppedFilesSetCleared);
        }
    }
    private DroppedFilesListenersProcessor droppedFilesListenersProcessor = new DroppedFilesListenersProcessor();
    public DragAndDropPanel(JComponent whenNoDropComponent, ResourceBundle resourceBundle){
        me = this;
        droppedFilesPanel = new JPanel();
        droppedFilesPanel.setLayout(new WrapLayout());
        JScrollPane jScrollPane = new JScrollPane(droppedFilesPanel);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                for (Component component :  droppedFilesPanel.getComponents()) {
                    if(component instanceof FileDisplayer)
                        ((FileDisplayer)component).setPreferredHeight(e.getComponent().getHeight() - 5);
                }
                droppedFilesPanel.setMaximumSize(new Dimension(e.getComponent().getWidth() - 5, -1));
                droppedFilesPanel.revalidate();

            }
        });

        this.setLayout(cardLayout);
        FileDisplayersMouseProcessor fileDisplayersMouseProcessor = new FileDisplayersMouseProcessor(this, resourceBundle);
        DropTarget dropTarget = new DropTarget(){
            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                try{
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    for(File droppedFile : (List<File>)( evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor)) ){
                        if(FileDisplayerGenerator.isThisFileHasValidFormat(droppedFile)){
                            // FIXME: 14.05.2023 создаётся меню для удаления и оно ничего не делает
                            FileDisplayer fileDisplayer = FileDisplayerGenerator.getFileDsiplayer(droppedFile);
                            JMenuItem removeFileMenuItem = new JMenuItem(resourceBundle.getString("menu.delete"));
                            JPopupMenu rightClickContextMenu = new JPopupMenu();
                            rightClickContextMenu.add(removeFileMenuItem);
                            fileDisplayer.setComponentPopupMenu(rightClickContextMenu);
                            droppedFilesPanel.add(fileDisplayer);
                            droppedFilesMap.put(droppedFile, fileDisplayer);
                            fileDisplayer.addMouseListener(fileDisplayersMouseProcessor);
                            droppedFilesListenersProcessor.droppedFileAdded(droppedFile);
                        }

                    }
                    if(!droppedFilesMap.isEmpty()){
                        evt.dropComplete(true);
                        droppedFilesPanel.revalidate();
                        cardLayout.show(me, "droppedFilesPanel");
                    }
                    else evt.dropComplete(false);
                }
                catch (Exception e){
                    e.printStackTrace();
                    evt.dropComplete(false);
                }
            }
        };
        this.setDropTarget(dropTarget);
        jScrollPane.setDropTarget(dropTarget);
        whenNoDropComponent.setDropTarget(dropTarget);
        this.add("droppedFilesPanel", jScrollPane);
        this.add("whenNoDropComponent", whenNoDropComponent);
        cardLayout.show(this, "whenNoDropComponent");
    }
    public Collection<File> getDroppedFiles(){
        return droppedFilesMap.keySet();
    }

    public boolean hasDroppedFiles(){
        return !droppedFilesMap.isEmpty();
    }
    public void addDroppedFilesSetListener(DroppedFilesListener droppedFilesListener){
        droppedFilesListenersProcessor.addListener(droppedFilesListener);
    }
    public void removeFileDisplayerFromDragAndDropPanel(FileDisplayer fileDisplayerForRemove){
        File fileForRemove = fileDisplayerForRemove.getDisplayedFile();
        droppedFilesPanel.remove(fileDisplayerForRemove);
        droppedFilesMap.remove(fileForRemove);
        droppedFilesPanel.invalidate();
        droppedFilesPanel.revalidate();
        if(droppedFilesMap.isEmpty()){
            cardLayout.show(me, "whenNoDropComponent");
            droppedFilesListenersProcessor.droppedFilesSetCleared();
        }
        else {
            droppedFilesListenersProcessor.droppedFileDeleted(fileForRemove);
        }
    }
    @Override
    public Collection<FileDisplayer> getFileDisplayers() {
        return droppedFilesMap.values();
    }

    public void clearDroppedFiles(){
        droppedFilesPanel.removeAll();
        droppedFilesPanel.revalidate();
        droppedFilesMap.clear();
        cardLayout.show(this, "whenNoDropComponent");
    }
}
