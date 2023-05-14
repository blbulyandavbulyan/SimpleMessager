package org.blbulyandavbulyan.smclient.ui.components.displayers.messagedisplaying.messagepanels.filemessagespanels;

import general.message.filemessages.FileMessage;
import org.blbulyandavbulyan.smclient.ui.components.displayers.messagedisplaying.messagepanels.MessagePanel;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

public abstract class FileMessagePanel extends MessagePanel {
    protected final JLabel fileNameLabel;
    protected JFrame viewFileWindow = null;
    protected boolean isViewWindowShow = false;
    protected FileMessagePanel(FileMessage fMsg, JFrame viewFileWindow, ResourceBundle rb) {
        super(fMsg, rb);
        this.viewFileWindow = viewFileWindow;
        if(viewFileWindow != null){
            viewFileWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            viewFileWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    isViewWindowShow = false;
                    super.windowClosing(e);
                }
            });
        }
        fileNameLabel = new JLabel(fMsg.getFileName());
    }
}
