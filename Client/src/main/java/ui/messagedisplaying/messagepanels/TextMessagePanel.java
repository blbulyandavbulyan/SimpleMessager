package ui.messagedisplaying.messagepanels;

import general.message.textmessage.TextMessage;

import javax.swing.*;

public class TextMessagePanel extends MessagePanel{

    public TextMessagePanel(TextMessage msg) {
        super(msg);
        JLabel textMessageLabel = new JLabel(msg.getMessageString());
        this.add(textMessageLabel);

    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        TextMessage textMessage = new TextMessage("abazbabbzbabbsdnakjdsdaw", "david", null);
        jFrame.add(new TextMessagePanel(textMessage));
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
