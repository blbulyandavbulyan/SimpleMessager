package org.blbulyandavbulyan.client.processings.audioprocessing.play;
import javax.sound.sampled.*;

public class PlayAudio{
    //класс воспроизводящий записанное аудио
    private class Listener implements LineListener {
        public void update(LineEvent ev) {
            if (ev.getType() == LineEvent.Type.STOP) {
                playing = false;
                clip.close();
                if(doAfterPlaying != null)doAfterPlaying.run();
            }
            else if(ev.getType() == LineEvent.Type.START){
                playing = true;
            }
        }

    }
    private Clip clip = null;
    private boolean playing = false;
    private final PlayAudio.Listener lineListener;
    private final Runnable doAfterPlaying;
    private final AudioFormat audioFormat;
    private final byte[] audioData;
    private final long microsecondsLength;
    public PlayAudio(byte[] audioData, AudioFormat audioFormat, Runnable doAfterPlaying){
        lineListener = new PlayAudio.Listener();
        this.doAfterPlaying = doAfterPlaying;
        this.audioData = audioData;
        this.audioFormat = audioFormat;
        float countOfSamples = (float)(audioData.length * 8)/audioFormat.getSampleSizeInBits();
        microsecondsLength = (long)(countOfSamples/audioFormat.getSampleRate()) * 1000_000;
    }
    public long getMicrosecondLength(){
        return microsecondsLength;
    }
    // проигрывается ли звук в данный момент
    public boolean isPlaying() {
        return playing;
    }

    public void play() {
        try{
            clip = AudioSystem.getClip();
            clip.open(audioFormat, audioData, 0, audioData.length);
            clip.addLineListener(lineListener);
            clip.setFramePosition(0);
            clip.start();
        }
        catch (LineUnavailableException e){
            throw new RuntimeException(e);
        }
    }

    // Останавливает воспроизведение
    public void stop() {
        if (playing) {
            clip.stop();
        }
    }
}
