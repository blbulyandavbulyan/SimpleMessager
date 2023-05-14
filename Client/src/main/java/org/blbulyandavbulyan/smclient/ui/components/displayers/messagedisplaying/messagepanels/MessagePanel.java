package org.blbulyandavbulyan.smclient.ui.components.displayers.messagedisplaying.messagepanels;

import org.blbulyandavbulyan.smgeneral.message.Message;
import org.blbulyandavbulyan.smclient.ui.components.displayers.messagedisplaying.exceptions.ResourceBundleIsNullException;
import org.blbulyandavbulyan.smclient.ui.components.displayers.messagedisplaying.exceptions.MessageIsNullException;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

public abstract class MessagePanel extends JPanel {
    protected JLabel senderLabel;
    protected ResourceBundle rb;
    protected final String senderName;
    protected final String sendingTimeString;
    protected MessagePanel(Message msg, ResourceBundle rb){
        if(msg == null)throw new MessageIsNullException();
        if(rb == null)throw new ResourceBundleIsNullException();
        this.rb = rb;
        this.senderName = msg.getSender();
        this.sendingTimeString = msg.getSendingTimeString();
        this.senderLabel = new JLabel(senderName);
    }
    public void addDoUserNameMuseClick(Runnable r){
        senderLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                r.run();
            }
        });
    }
    public void setUserNameToolTip(String toolTip){
        senderLabel.setToolTipText(toolTip);
    }
}
