package ui.customuicomponents.draganddroppanel;

import ui.customuicomponents.filedisplayers.FileDisplayer;
import ui.customuicomponents.filedisplayers.FileDisplayerGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.List;

public class DragAndDropPanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final Set<File> droppedFilesSet = new HashSet<>();
    private final JPanel droppedFilesPanel;
    private final DragAndDropPanel me;
    public DragAndDropPanel(JComponent whenNoDropComponent){
        me = this;
        droppedFilesPanel = new JPanel();
        droppedFilesPanel.setLayout(new BoxLayout(droppedFilesPanel, BoxLayout.Y_AXIS));
        this.setLayout(cardLayout);
        this.setDropTarget(new DropTarget(){
            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                try{
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    DataFlavor[] dataFlavors = evt.getTransferable().getTransferDataFlavors();
//                    for (DataFlavor dataFlavor : dataFlavors) {
//                        if(!isThisFormatValid(dataFlavor)){
//                            evt.dropComplete(false);
//                            return;
//                        }
//                    }
                    for(File droppedFile : (List<File>)( evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor)) ){
                        if(FileDisplayerGenerator.isThisFileHasValidFormat(droppedFile)){
                            FileDisplayer fileDisplayer = FileDisplayerGenerator.getFileDsiplayer(droppedFile);
                            JButton removeButton = new JButton("X");
                            fileDisplayer.add(removeButton);
                            droppedFilesPanel.add(fileDisplayer);
                            droppedFilesSet.add(droppedFile);
                            removeButton.addActionListener((ae)->{
                                droppedFilesSet.remove(droppedFile);
                                droppedFilesPanel.remove(fileDisplayer);
                                droppedFilesPanel.revalidate();
                                if(droppedFilesSet.isEmpty()){
                                    cardLayout.show(me, "whenNoDropComponent");
                                }
                            });
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
        });
        this.add("droppedFilesPanel", new JScrollPane(droppedFilesPanel));
        this.add("whenNoDropComponent", whenNoDropComponent);
        cardLayout.show(this, "whenNoDropComponent");
    }
    public Set<File> getDroppedFiles(){
        return droppedFilesSet;
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
