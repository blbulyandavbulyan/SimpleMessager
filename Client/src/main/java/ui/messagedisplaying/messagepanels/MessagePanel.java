package ui.messagedisplaying.messagepanels;

import general.message.Message;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

public abstract class MessagePanel extends JPanel {
    protected JLabel senderLabel;
    protected JLabel sendTimeLabel;
    protected ResourceBundle rb;
    protected MessagePanel(Message msg, ResourceBundle rb){
        if(rb == null)throw new NullPointerException();
        this.rb = rb;
        senderLabel = new JLabel(msg.getSender());
        sendTimeLabel = new JLabel(msg.getSendingTimeString());
        if(rb.containsKey("messagePanel.SenderLabelTooltip"))senderLabel.setToolTipText(rb.getString("messagePanel.SenderLabelTooltip"));
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
