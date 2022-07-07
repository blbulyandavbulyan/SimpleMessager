package audioprocessing;

import javax.sound.sampled.AudioFormat;
import javax.swing.*;

public class SimplePlayButton extends JButton {
    private final PlayAudio playAudio;

    public SimplePlayButton(byte [] audioData, AudioFormat audioFormat) {
        this.playAudio = new PlayAudio(audioData, audioFormat,  ()-> this.setText("Воспроизвести"));
        this.addActionListener(l->{
            if(playAudio.isPlaying()){
                playAudio.stop();
                this.setText("Воспроизвести");
            }
            else {
                playAudio.play();
                this.setText("Остановить");
            }
        });
    }
}
