package org.blbulyandavbulyan.smclient.ui.components.displayers.messagedisplaying.messagepanels.standartmessagepanels;

import org.blbulyandavbulyan.smgeneral.message.textmessage.TextMessage;

import javax.swing.*;
import java.util.ResourceBundle;

public class TextMessagePanel extends StandartMessagePanel {

    public TextMessagePanel(TextMessage msg, ResourceBundle rb) {
        super(msg, rb);
        JLabel textMessageLabel = new JLabel(msg.getMessageString());
        this.add(textMessageLabel);

    }
}
