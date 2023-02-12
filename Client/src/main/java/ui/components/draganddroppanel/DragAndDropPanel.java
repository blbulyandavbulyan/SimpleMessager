package ui.components.draganddroppanel;

import ui.components.displayers.filedisplayers.FileDisplayer;
import ui.components.displayers.generators.FileDisplayerGenerator;
import ui.components.draganddroppanel.interfaces.DroppedFilesListener;
import ui.components.draganddroppanel.layouts.WrapLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

public class DragAndDropPanel extends JPanel implements DroppedFileControllerInterface {
    private final CardLayout cardLayout = new CardLayout();
    private final Map<File, FileDisplayer> droppedFilesMap = new HashMap<>();
    private final JPanel droppedFilesPanel;
    private final DragAndDropPanel me;
    private final Set<DroppedFilesListener> droppedFilesListeners = new HashSet<>();
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
                            FileDisplayer fileDisplayer = FileDisplayerGenerator.getFileDsiplayer(droppedFile);
                            JMenuItem removeFileMenuItem = new JMenuItem(resourceBundle.getString("menu.delete"));
                            JPopupMenu rightClickContextMenu = new JPopupMenu();
                            rightClickContextMenu.add(removeFileMenuItem);
                            fileDisplayer.setComponentPopupMenu(rightClickContextMenu);
                            droppedFilesPanel.add(fileDisplayer);
                            droppedFilesMap.put(droppedFile, fileDisplayer);
                            fileDisplayer.addMouseListener(fileDisplayersMouseProcessor);
                            notifyAllDroppedFilesSetListeners("droppedFileAdded", droppedFile);
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
    public Set<File> getDroppedFiles(){
        return droppedFilesMap.keySet();
    }

    public boolean hasDroppedFiles(){
        return !droppedFilesMap.isEmpty();
    }
    public void addDroppedFilesSetListener(DroppedFilesListener droppedFilesListener){
        if(droppedFilesListener != null) droppedFilesListeners.add(droppedFilesListener);
    }
    public void removeDroppedFilesSetListener(DroppedFilesListener droppedFilesListener){
        droppedFilesListeners.remove(droppedFilesListener);
    }
    private void notifyAllDroppedFilesSetListeners(String droppedFilesListenerMethodName, Object ... args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method droppedFileListenerMethod;
        if(args != null){
            Class[] argClasses = new Class[args.length];
            for(int i = 0; i < argClasses.length; i++){
                argClasses[i] = args[i].getClass();
            }
            droppedFileListenerMethod = DroppedFilesListener.class.getMethod(droppedFilesListenerMethodName, argClasses);
        }
        else droppedFileListenerMethod = DroppedFilesListener.class.getMethod(droppedFilesListenerMethodName);
        for (DroppedFilesListener droppedFilesListener : droppedFilesListeners) {
            if(args != null)droppedFileListenerMethod.invoke(droppedFilesListener, args);
            else droppedFileListenerMethod.invoke(droppedFilesListener);
        }
    }
    public void removeFileDisplayerFromDragAndDropPanel(FileDisplayer fileDisplayerForRemove){
        File fileForRemove = fileDisplayerForRemove.getDisplayedFile();
        droppedFilesPanel.remove(fileDisplayerForRemove);
        droppedFilesMap.remove(fileForRemove);
        droppedFilesPanel.invalidate();
        droppedFilesPanel.revalidate();
        if(droppedFilesMap.isEmpty()){
            cardLayout.show(me, "whenNoDropComponent");
            try {
                notifyAllDroppedFilesSetListeners("droppedFilesSetCleared");
            } catch (InvocationTargetException | IllegalAccessException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                notifyAllDroppedFilesSetListeners("droppedFileDeleted", fileForRemove);
            } catch (InvocationTargetException | IllegalAccessException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
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
//    public static void main(String[] args) {
//        JFrame jFrame = new JFrame();
//        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        DragAndDropPanel dragAndDropPanel = new DragAndDropPanel(new JLabel("Перетащите файлы сюда"));
//        jFrame.getContentPane().add(dragAndDropPanel);
//        jFrame.pack();
//        jFrame.setVisible(true);
//    }
}
