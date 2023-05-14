package org.blbulyandavbulyan.client.ui.components.displayers.messagedisplaying.messagepanels.standartmessagepanels;

import general.message.Message;
import org.blbulyandavbulyan.client.ui.components.displayers.messagedisplaying.messagepanels.MessagePanel;

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
