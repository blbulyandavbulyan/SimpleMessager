package audioprocessing;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

public class RecordAudioThread extends Thread implements Closeable {
    private boolean recordStopped = false;
    private boolean recordPaused;
    private final Object pauseAndResumeMonitor;
    private final Object recordFinishedMonitor;
    private final ByteArrayOutputStream out;
    private final TargetDataLine microphone;
    public RecordAudioThread(AudioFormat audioFormat, boolean recordPaused) throws LineUnavailableException {
        this.recordPaused = recordPaused;
        pauseAndResumeMonitor = new Object();
        recordFinishedMonitor = new Object();
        out = new ByteArrayOutputStream();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(audioFormat);
        start();
    }

    @Override
    public void run() {
        byte[] data = new byte[microphone.getBufferSize() / 5];
        try{
            int bytesRead = 0;
            while (!recordStopped){
                if(recordPaused){
                    synchronized (pauseAndResumeMonitor){
                        try {
                            pauseAndResumeMonitor.wait();

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
                int numBytesRead;
                int CHUNK_SIZE = 1024;
                numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                bytesRead = bytesRead + numBytesRead;
                System.out.println(bytesRead);
                out.write(data, 0, numBytesRead);

            }
        }
       catch (Exception e){
            e.printStackTrace();
       }
        finally {
            synchronized (recordFinishedMonitor){
                recordFinishedMonitor.notify();
            }

        }

    }
    public void clear(){
        out.reset();
        //microphone.flush();
    }
    public void finishRecord(){
        synchronized (recordFinishedMonitor){
            recordFinishedMonitor.notify();
        }
    }
    public void stopRecord(){
        recordStopped = true;
    }
    public void pauseRecord() {
        recordPaused = true;
        microphone.stop();
    }
    public void resumeRecord(){
        synchronized (pauseAndResumeMonitor) {
            recordPaused = false;
            microphone.start();
            pauseAndResumeMonitor.notify();
        }

    }
    public byte[] getAudio() throws InterruptedException {
        synchronized (recordFinishedMonitor){
            recordFinishedMonitor.wait();
            return out.toByteArray();
        }

    }
    public void close() throws IOException {
        microphone.close();
        out.close();
    }
}
