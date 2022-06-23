package ui.messagedisplaying.messagepanels;

import audioprocessing.PlayVoiceMessage;
import general.message.voicemessage.VoiceMessage;

import javax.swing.*;

public class VoiceMessagePanel extends MessagePanel {
    private final JButton playButton;
    private final JLabel audioMessageInformation;
    private final PlayVoiceMessage playVoiceMessage;
    public VoiceMessagePanel(VoiceMessage voiceMessage) {
        super(voiceMessage);
        playButton = new JButton("Воспроизвести");
        audioMessageInformation = new JLabel();
        this.add(audioMessageInformation);
        this.add(playButton);
        audioMessageInformation.setVisible(false);
        playButton.setEnabled(false);
        playVoiceMessage = new PlayVoiceMessage(voiceMessage, ()->playButton.setText("Воспроизвести"));
        tryToInitPlayVoiceMessage(voiceMessage);
        playButton.addActionListener(l->{
            if(playVoiceMessage.isPlaying()){
                playVoiceMessage.stop();
                playButton.setText("Воспроизвести");
            }
            else {
                playVoiceMessage.play();
                playButton.setText("Остановить");
            }
        });
    }
    private void tryToInitPlayVoiceMessage(final VoiceMessage voiceMessage){
        boolean result = playVoiceMessage.init(voiceMessage);
        if(result){
            playButton.setEnabled(true);
            audioMessageInformation.setText(playVoiceMessage.getStringLength());
            audioMessageInformation.setVisible(true);
        }
        else SwingUtilities.invokeLater(()->tryToInitPlayVoiceMessage(voiceMessage));
    }
    public static void main(String[] args) {

    }
}
