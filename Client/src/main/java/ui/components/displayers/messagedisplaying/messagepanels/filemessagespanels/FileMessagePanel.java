package ui.components.displayers.messagedisplaying.messagepanels.filemessagespanels;

import general.message.filemessages.FileMessage;
import ui.components.displayers.messagedisplaying.messagepanels.MessagePanel;

import javax.swing.*;
import java.util.ResourceBundle;

public abstract class FileMessagePanel extends MessagePanel {
    protected JLabel fileNameLabel;
    protected FileMessagePanel(FileMessage fMsg, ResourceBundle rb) {
        super(fMsg, rb);
        fileNameLabel = new JLabel(fMsg.getFileName());
    }
}
