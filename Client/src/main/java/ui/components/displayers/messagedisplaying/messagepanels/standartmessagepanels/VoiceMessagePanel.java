package ui.components.displayers.messagedisplaying.messagepanels.standartmessagepanels;

import processings.audioprocessing.play.PlayVoiceMessage;
import general.message.voicemessage.VoiceMessage;

import javax.swing.*;
import java.util.ResourceBundle;

public class VoiceMessagePanel extends StandartMessagePanel {
    private final JButton playButton;
    private final JLabel audioMessageInformation;
    private final PlayVoiceMessage playVoiceMessage;
    public VoiceMessagePanel(VoiceMessage voiceMessage, ResourceBundle rb) {
        super(voiceMessage, rb);
        playButton = new JButton(rb.getString("voiceMessagePanel.Play"));
        audioMessageInformation = new JLabel();
        this.add(audioMessageInformation);
        this.add(playButton);
        audioMessageInformation.setVisible(false);
        playButton.setEnabled(false);
        playVoiceMessage = new PlayVoiceMessage(voiceMessage, ()->playButton.setText(rb.getString("voiceMessagePanel.Play")));
        tryToInitPlayVoiceMessage(voiceMessage);
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
    }
    private void tryToInitPlayVoiceMessage(final VoiceMessage voiceMessage){
        boolean result = playVoiceMessage.init(voiceMessage.getAudioData(), voiceMessage.getAudioFormat());
        if(result){
            playButton.setEnabled(true);
            audioMessageInformation.setText(playVoiceMessage.getStringLength());
            audioMessageInformation.setVisible(true);
        }
        else SwingUtilities.invokeLater(()->tryToInitPlayVoiceMessage(voiceMessage));
    }
}
