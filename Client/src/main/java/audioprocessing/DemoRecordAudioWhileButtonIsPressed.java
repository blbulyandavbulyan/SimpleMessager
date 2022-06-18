package audioprocessing;

import general.message.voicemessage.VoiceMessage;
import ui.messagedisplaying.VoiceMessagePanel;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DemoRecordAudioWhileButtonIsPressed {
   static private class RecordMouseAdapter extends MouseAdapter {
        private RecordAudioThread recordAudioThread;
        public RecordMouseAdapter(AudioFormat audioFormat) throws LineUnavailableException {
            this.recordAudioThread = new RecordAudioThread(audioFormat, true);
        }
        @Override
        public void mousePressed(MouseEvent e) {
            recordAudioThread.clear();
            recordAudioThread.resumeRecord();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            recordAudioThread.pauseRecord();
            recordAudioThread.finishRecord();

        }
        public byte[] getAudio() throws InterruptedException {
            return recordAudioThread.getAudio();
        }
    }
    public static void main(String[] args) throws LineUnavailableException, InterruptedException {
        JFrame jFrame = new JFrame();
        JButton recordButton = new JButton("Записать");
        AudioFormat audioFormat =  new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, true);
        RecordMouseAdapter recordMouseAdapter = new RecordMouseAdapter(audioFormat);
        recordButton.addMouseListener(recordMouseAdapter);
        jFrame.add(recordButton);
        jFrame.setSize(new Dimension(200, 200));
        jFrame.setVisible(true);

        JFrame jFrame1 = new JFrame();
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        jpanel.setAlignmentY(Component.TOP_ALIGNMENT);
        jpanel.add(new VoiceMessagePanel(new VoiceMessage("david", null, recordMouseAdapter.getAudio(), audioFormat)));

        jpanel.add(new VoiceMessagePanel(new VoiceMessage("test", null, recordMouseAdapter.getAudio(), audioFormat)));

        jFrame1.getContentPane().add(jpanel);
        jFrame1.pack();
        jFrame1.setVisible(true);
    }
}
