package ui.components.draganddroppanel;

import ui.components.displayers.filedisplayers.FileDisplayer;
import ui.components.displayers.generators.FileDisplayerGenerator;

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

public class DragAndDropPanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final Set<File> droppedFilesSet = new HashSet<>();
    private final JPanel droppedFilesPanel;
    private final DragAndDropPanel me;
    private final Set<DroppedFilesListener> droppedFilesListeners = new HashSet<>();
    public DragAndDropPanel(JComponent whenNoDropComponent){
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
        DropTarget dropTarget = new DropTarget(){
            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                try{
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    for(File droppedFile : (List<File>)( evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor)) ){
                        if(FileDisplayerGenerator.isThisFileHasValidFormat(droppedFile)){
                            FileDisplayer fileDisplayer = FileDisplayerGenerator.getFileDsiplayer(droppedFile);

                            droppedFilesPanel.add(fileDisplayer);
                            droppedFilesSet.add(droppedFile);
                            fileDisplayer.setRemoveFromDragAndDropPanelAction(()->{
                                droppedFilesSet.remove(droppedFile);
                                droppedFilesPanel.remove(fileDisplayer);
                                droppedFilesPanel.invalidate();
                                droppedFilesPanel.revalidate();
                                if(droppedFilesSet.isEmpty()){
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
                                        notifyAllDroppedFilesSetListeners("droppedFileDeleted", droppedFile);
                                    } catch (InvocationTargetException | IllegalAccessException |
                                             NoSuchMethodException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                            notifyAllDroppedFilesSetListeners("droppedFileAdded", droppedFile);
                        }

                    }
                    if(!droppedFilesSet.isEmpty()){
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
        return droppedFilesSet;
    }

    public boolean hasDroppedFiles(){
        return !droppedFilesSet.isEmpty();
    }
    public void addDroppedFilesSetListener(DroppedFilesListener droppedFilesListener){
        if(droppedFilesListener != null) droppedFilesListeners.add(droppedFilesListener);
    }
    public void removeDroppedFilesSetListener(DroppedFilesListener droppedFilesListener){
        droppedFilesListeners.remove(droppedFilesListener);
    }
    private void notifyAllDroppedFilesSetListeners(String droppedFilesListenerMethodName, Object ... args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method droppedFileListenerMethod = null;
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
    public void clearDroppedFiles(){
        droppedFilesPanel.removeAll();
        droppedFilesPanel.revalidate();
        droppedFilesSet.clear();
        cardLayout.show(this, "whenNoDropComponent");
    }
    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        DragAndDropPanel dragAndDropPanel = new DragAndDropPanel(new JLabel("Перетащите файлы сюда"));
        jFrame.getContentPane().add(dragAndDropPanel);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
