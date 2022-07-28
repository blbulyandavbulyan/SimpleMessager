package ui.components.displayers.messagedisplaying.messagepanels.filemessagespanels;

import general.message.filemessages.imagefilesmessages.ImageFileMessage;
import ui.components.displayers.general.areas.MiniatureImageArea;
import ui.components.displayers.general.viewwindows.ViewImageWindow;

import java.util.ResourceBundle;

public class ImageFileMessagePanel extends FileMessagePanel {
    protected final MiniatureImageArea miniatureImageArea;
    protected final ViewImageWindow viewImageWindow;
    public ImageFileMessagePanel(ImageFileMessage iMsg, ResourceBundle rb) {
        super(iMsg, rb);
        miniatureImageArea  = new MiniatureImageArea(iMsg.getImageIcon(), 100);
        viewImageWindow = new ViewImageWindow(iMsg.getImageIcon());
    }
}
