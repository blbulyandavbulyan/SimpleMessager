package processings.audioprocessing.record;


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

    public RecordStopButtonActionListener(AudioFormat audioFormat, long minRecordLengthInSeconds, Consumer<RECORD_STATES> recordStateChangeAction, Consumer<byte[]> audioDataConsumer) throws LineUnavailableException {
        recordAudioThread = new RecordAudioThread(audioFormat, true);
        this.minRecordLengthInSeconds = minRecordLengthInSeconds;
        this.recordStateChangeAction = recordStateChangeAction;
        this.audioDataConsumer = audioDataConsumer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (recordStarted) {
                recordLengthInSeconds = (System.currentTimeMillis() - recordLengthInSeconds) / 1000;
                if (recordLengthInSeconds > minRecordLengthInSeconds) {
                    recordStarted = false;
                    byte [] audioData = recordAudioThread.stopRecord();
                    if(audioDataConsumer != null)audioDataConsumer.accept(audioData);
                    if (recordStateChangeAction != null) recordStateChangeAction.accept(RECORD_STATES.RECORD_STOPPED);
                }
            } else {
                recordStarted = true;
                recordAudioThread.startRecord();
                recordLengthInSeconds = System.currentTimeMillis();
                if (recordStateChangeAction != null) {
                    recordStateChangeAction.accept(RECORD_STATES.RECORD_STARTED);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public byte[] getAudio() throws InterruptedException {
        return recordAudioThread.getAudio();
    }
}
