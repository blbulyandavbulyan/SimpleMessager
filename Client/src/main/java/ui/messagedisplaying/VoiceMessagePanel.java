package ui.messagedisplaying;

import audioprocessing.PlayVoiceMessage;
import general.message.voicemessage.VoiceMessage;

import javax.swing.*;

public class VoiceMessagePanel extends MessagePanel {
    private final JButton playButton;
    //private final JLabel audiMessageInformation;
    private final PlayVoiceMessage playVoiceMessage;
    public VoiceMessagePanel(VoiceMessage voiceMessage) {
        super(voiceMessage);
        playButton = new JButton("Воспроизвести");
        //fixme
        this.add(playButton);
        playVoiceMessage = new PlayVoiceMessage(voiceMessage, ()->playButton.setText("Воспроизвести"));
        if(!playVoiceMessage.isReleased()) {
            playButton.setEnabled(false);
            SwingUtilities.invokeLater(()->tryToInitPlayVoiceMessage(voiceMessage));
        }
        else {
            //audiMessageInformation = new JLabel("%d:%d".formatted());
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
        }
        else SwingUtilities.invokeLater(()->tryToInitPlayVoiceMessage(voiceMessage));
        return playVoiceMessage.isReleased();
    }
    public static void main(String[] args) {

    }
}
