package ui.messagedisplaying.messagepanels;

import general.message.Message;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class MessagePanel extends JPanel {
    protected JLabel senderLabel;
    protected JLabel sendTimeLabel;
    protected MessagePanel(Message msg){
        senderLabel = new JLabel(msg.getSender());
        sendTimeLabel = new JLabel(msg.getSendingTimeString());
        this.add(senderLabel);
        this.add(sendTimeLabel);
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
