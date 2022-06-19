package ui.messagedisplaying;

import audioprocessing.PlayVoiceMessage;
import general.message.voicemessage.VoiceMessage;

import javax.sound.sampled.Mixer;
import javax.swing.*;

public class VoiceMessagePanel extends MessagePanel {
    private final JButton playButton;
    private JLabel audiMessageInformation;
    private final PlayVoiceMessage playVoiceMessage;
    public VoiceMessagePanel(VoiceMessage voiceMessage, Mixer mixer) {
        super(voiceMessage);
        playButton = new JButton("Воспроизвести");
        audiMessageInformation = new JLabel();
        this.add(audiMessageInformation);
        this.add(playButton);
        playVoiceMessage = new PlayVoiceMessage(voiceMessage, ()->playButton.setText("Воспроизвести"), mixer);


        if(!playVoiceMessage.isReleased()) {
            playButton.setEnabled(false);
            audiMessageInformation.setVisible(false);
            SwingUtilities.invokeLater(()->tryToInitPlayVoiceMessage(voiceMessage));
        }
        else {
            audiMessageInformation.setText(playVoiceMessage.getStringLength());
        }
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
    private boolean tryToInitPlayVoiceMessage(final VoiceMessage voiceMessage){
        boolean result = playVoiceMessage.init(voiceMessage);
        if(result){
            playButton.setEnabled(true);
            audiMessageInformation.setText(playVoiceMessage.getStringLength());
            audiMessageInformation.setVisible(true);
        }
        else SwingUtilities.invokeLater(()->tryToInitPlayVoiceMessage(voiceMessage));
        return playVoiceMessage.isReleased();
    }
    public static void main(String[] args) {

    }
}
