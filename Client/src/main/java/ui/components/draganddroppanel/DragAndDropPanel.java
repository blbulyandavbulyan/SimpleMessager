package ui.components.draganddroppanel;

import general.message.Message;
import general.message.filemessages.FileMessage;
import serverconnection.ServerConnection;
import serverconnection.interfaces.MessageSender;
import ui.components.displayers.filedisplayers.FileDisplayer;
import ui.components.displayers.generators.FileDisplayerGenerator;
import ui.components.draganddroppanel.exceptions.MessageSenderIsNullException;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
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
        droppedFilesPanel.setLayout(new FlowLayout());
        this.setLayout(cardLayout);
        DropTarget dropTarget = new DropTarget(){
            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                try{
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
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
        };
        this.setDropTarget(dropTarget);
        whenNoDropComponent.setDropTarget(dropTarget);
        this.add("droppedFilesPanel", droppedFilesPanel);
        this.add("whenNoDropComponent", whenNoDropComponent);
        cardLayout.show(this, "whenNoDropComponent");
    }
    public Set<File> getDroppedFiles(){
        return droppedFilesSet;
    }
    public Collection<FileMessage> getFileMessages(){
        LinkedList<FileMessage> fileMessages = new LinkedList<>();

        return fileMessages;
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
