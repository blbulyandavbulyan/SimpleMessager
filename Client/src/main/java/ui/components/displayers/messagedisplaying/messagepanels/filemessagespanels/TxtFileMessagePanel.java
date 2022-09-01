package ui.components.displayers.messagedisplaying.messagepanels.filemessagespanels;

import general.message.filemessages.FileMessage;

import javax.swing.*;
import java.util.ResourceBundle;

public class TxtFileMessagePanel extends FileMessagePanel{
    protected TxtFileMessagePanel(FileMessage fMsg, ResourceBundle rb) {
        super(fMsg, null, rb);
    }
}
