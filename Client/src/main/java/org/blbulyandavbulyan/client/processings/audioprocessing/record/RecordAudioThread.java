package org.blbulyandavbulyan.client.processings.audioprocessing.record;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

public class RecordAudioThread extends Thread{
    //это поток для записи аудио
    private boolean threadStopped = false;
    private final Object recordFinishedMonitor;
    private final ByteArrayOutputStream out;
    private final AudioFormat audioFormat;
    public RecordAudioThread(AudioFormat audioFormat){
        recordFinishedMonitor = new Object();
        int requiredBuffsize = (int) (audioFormat.getFrameSize()*audioFormat.getFrameRate()*audioFormat.getChannels())*120;
        out = requiredBuffsize > 0 ?  new ByteArrayOutputStream(requiredBuffsize) : new ByteArrayOutputStream();
        this.audioFormat = audioFormat;
        start();
    }
    @Override
    public void run() {
        final int CHUNK_SIZE = 1024;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        try(TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info)){
            synchronized (recordFinishedMonitor){
                byte[] data = new byte[CHUNK_SIZE];
                microphone.open(audioFormat);
//                microphone.open();
                microphone.start();
                int bytesRead = 0;
                while (!threadStopped){
                    int numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                    bytesRead = bytesRead + numBytesRead;
                    System.out.println(bytesRead);
                    out.write(data, 0, numBytesRead);
                }
            }
        }
        catch (Exception e){
             throw new RuntimeException(e);
        }
    }
    private void stopThread(){
        threadStopped = true;
    }
    public byte[] getAudio(){
        stopThread();
        synchronized (recordFinishedMonitor){
            return out.toByteArray();
        }
    }
}
