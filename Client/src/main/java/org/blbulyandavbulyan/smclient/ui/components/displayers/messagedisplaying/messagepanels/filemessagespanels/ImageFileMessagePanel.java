package org.blbulyandavbulyan.smclient.ui.components.displayers.messagedisplaying.messagepanels.filemessagespanels;

import general.message.filemessages.ImageFileMessage;
import org.blbulyandavbulyan.smclient.ui.components.displayers.general.areas.MiniatureImageArea;
import org.blbulyandavbulyan.smclient.ui.components.displayers.general.viewwindows.ViewImageWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

public class ImageFileMessagePanel extends FileMessagePanel {
    protected final MiniatureImageArea miniatureImageArea;
    public ImageFileMessagePanel(ImageFileMessage iMsg, ResourceBundle rb) {
        super(iMsg, new ViewImageWindow(iMsg.getImageIcon()), rb);
        miniatureImageArea  = new MiniatureImageArea(iMsg.getImageIcon(), 100, false);
        miniatureImageArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                viewFileWindow.setVisible(isViewWindowShow = !isViewWindowShow);
            }
        });
        JPanel sendingTimeAndSenderPanel = new JPanel();
        sendingTimeAndSenderPanel.add(new JLabel(sendingTimeString));
        sendingTimeAndSenderPanel.add(senderLabel);
        this.setLayout(new BorderLayout());
        this.add(sendingTimeAndSenderPanel, BorderLayout.NORTH);
        this.add(miniatureImageArea, BorderLayout.CENTER);
    }
    public void setPreferredHeight(int preferredHeight){
        miniatureImageArea.setPreferredHeight(preferredHeight);
    }
}
