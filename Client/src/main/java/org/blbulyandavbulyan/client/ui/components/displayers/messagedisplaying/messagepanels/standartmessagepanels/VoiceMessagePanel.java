package org.blbulyandavbulyan.client.ui.components.displayers.messagedisplaying.messagepanels.standartmessagepanels;

import org.blbulyandavbulyan.client.processings.audioprocessing.play.PlayVoiceMessage;
import general.message.voicemessage.VoiceMessage;

import javax.swing.*;
import java.util.ResourceBundle;

public class VoiceMessagePanel extends StandartMessagePanel {
    private final JButton playButton;
    private final PlayVoiceMessage playVoiceMessage;
    public VoiceMessagePanel(VoiceMessage voiceMessage, ResourceBundle rb) {
        super(voiceMessage, rb);
        playButton = new JButton(rb.getString("voiceMessagePanel.Play"));
        JLabel audioMessageInformation = new JLabel();
        this.add(audioMessageInformation);
        this.add(playButton);
        audioMessageInformation.setVisible(false);
        playButton.setEnabled(false);
        playVoiceMessage = new PlayVoiceMessage(voiceMessage, ()->playButton.setText(rb.getString("voiceMessagePanel.Play")));
        playButton.addActionListener(l->{
            if(playVoiceMessage.isPlaying()){
                playVoiceMessage.stop();
                playButton.setText(rb.getString("voiceMessagePanel.Play"));
            }
            else {
                playVoiceMessage.play();
                playButton.setText(rb.getString("voiceMessagePanel.Stop"));
            }
        });
        playButton.setEnabled(true);
        audioMessageInformation.setText(playVoiceMessage.getFormattedAudioMessageLength());
        audioMessageInformation.setVisible(true);
    }
}
