package ui.messagedisplaying;

import general.message.Message;

import javax.swing.*;

public abstract class MessagePanel extends JPanel {
    protected JLabel senderLabel;
    protected JLabel sendTimeLabel;
    protected MessagePanel(Message msg){
        senderLabel = new JLabel(msg.getSender());
        sendTimeLabel = new JLabel(msg.getSendingTimeString());
        this.add(senderLabel);
        this.add(sendTimeLabel);
    }
}
