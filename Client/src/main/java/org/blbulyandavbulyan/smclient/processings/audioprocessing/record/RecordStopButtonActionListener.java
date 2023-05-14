package org.blbulyandavbulyan.smclient.processings.audioprocessing.record;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
public class RecordStopButtonActionListener implements ActionListener {
    //это ActionListener для кнопки записи
    public enum RECORD_STATES {RECORD_STARTED, RECORD_STOPPED}

    private boolean recordStarted = false;
    private final long minRecordLengthInSeconds;
    private long recordLengthInSeconds;
    RecordAudioThread recordAudioThread;
    private final Consumer<RECORD_STATES> recordStateChangeAction;
    private final Consumer<byte[]> audioDataConsumer;
    private final AudioFormat audioFormat;

    public RecordStopButtonActionListener(AudioFormat audioFormat, long minRecordLengthInSeconds, Consumer<RECORD_STATES> recordStateChangeAction, Consumer<byte[]> audioDataConsumer) throws LineUnavailableException {
        this.audioFormat = audioFormat;
        this.minRecordLengthInSeconds = minRecordLengthInSeconds;
        this.recordStateChangeAction = recordStateChangeAction;
        this.audioDataConsumer = audioDataConsumer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (recordStarted) {
            recordLengthInSeconds = (System.currentTimeMillis() - recordLengthInSeconds) / 1000;
            if (recordLengthInSeconds > minRecordLengthInSeconds) {
                recordStarted = false;
                byte[] audioData = recordAudioThread.getAudio();
                if (audioDataConsumer != null) audioDataConsumer.accept(audioData);
                if (recordStateChangeAction != null) recordStateChangeAction.accept(RECORD_STATES.RECORD_STOPPED);
            }
        } else {
            recordStarted = true;
            recordAudioThread = new RecordAudioThread(audioFormat);
            recordLengthInSeconds = System.currentTimeMillis();
            if (recordStateChangeAction != null) {
                recordStateChangeAction.accept(RECORD_STATES.RECORD_STARTED);
            }
        }
    }

    public byte[] getAudio(){
        return recordAudioThread.getAudio();
    }
}
