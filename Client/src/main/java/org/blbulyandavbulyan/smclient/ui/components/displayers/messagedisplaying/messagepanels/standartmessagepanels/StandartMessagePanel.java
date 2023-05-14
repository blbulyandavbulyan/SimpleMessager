package org.blbulyandavbulyan.smclient.ui.components.displayers.messagedisplaying.messagepanels.standartmessagepanels;

import org.blbulyandavbulyan.smgeneral.message.Message;
import org.blbulyandavbulyan.smclient.ui.components.displayers.messagedisplaying.messagepanels.MessagePanel;

import javax.swing.*;
import java.util.ResourceBundle;

public abstract class StandartMessagePanel extends MessagePanel {
    protected StandartMessagePanel(Message msg, ResourceBundle rb) {
        super(msg, rb);
        JLabel sendTimeLabel = new JLabel(msg.getSendingTimeString());
        if(rb.containsKey("messagePanel.SenderLabelTooltip"))senderLabel.setToolTipText(rb.getString("messagePanel.SenderLabelTooltip"));
        this.add(senderLabel);
        this.add(sendTimeLabel);
    }
}
