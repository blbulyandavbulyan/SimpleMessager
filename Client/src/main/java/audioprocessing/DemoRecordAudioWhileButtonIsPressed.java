package audioprocessing;

import general.message.voicemessage.VoiceMessage;
import ui.messagedisplaying.VoiceMessagePanel;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class DemoRecordAudioWhileButtonIsPressed {

    public static void main(String[] args) throws LineUnavailableException, InterruptedException {
        JFrame jFrame = new JFrame();
        JButton recordButton = new JButton("Записать");
        AudioFormat audioFormat =  new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, true);
        RecordStopButtonActionListener recordStopActionListener = new RecordStopButtonActionListener(audioFormat, 3, (record_state)->{
            switch (record_state){
                case RECORD_STARTED -> recordButton.setText("Остановить");
                case RECORD_STOPPED -> recordButton.setText("Записать");
            }
        }, null ,null);
        //recordButton.addMouseListener(recordStopActionListener);
        recordButton.addActionListener(recordStopActionListener);
        jFrame.add(recordButton);
        jFrame.setSize(new Dimension(200, 200));
        jFrame.setVisible(true);
        Mixer mixer = AudioSystem.getMixer(null);
        JFrame jFrame1 = new JFrame();
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        jpanel.setAlignmentY(Component.TOP_ALIGNMENT);
        jpanel.add(new VoiceMessagePanel(new VoiceMessage("david", null, recordStopActionListener.getAudio(), audioFormat)));

        jpanel.add(new VoiceMessagePanel(new VoiceMessage("test", null, recordStopActionListener.getAudio(), audioFormat)));
        jpanel.add(new VoiceMessagePanel(new VoiceMessage("test", null, recordStopActionListener.getAudio(), audioFormat)));
        jFrame1.getContentPane().add(jpanel);
        jFrame1.pack();
        jFrame1.setVisible(true);
    }
}
