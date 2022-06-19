package audioprocessing;

import general.message.voicemessage.VoiceMessage;

import javax.sound.sampled.*;

public class PlayVoiceMessage implements AutoCloseable {
    private boolean released = false;
    private Clip clip = null;
    private FloatControl volumeControl = null;
    private boolean playing = false;
    private final Runnable doAfterPlaying;
    public PlayVoiceMessage(VoiceMessage voiceMessage, Runnable doAfterPlaying, Mixer mixer){
        try{

            clip = (Clip) mixer.getLine(new DataLine.Info(Clip.class, voiceMessage.getAudioFormat()));
            clip.open(voiceMessage.getAudioFormat(), voiceMessage.getAudioData(), 0, voiceMessage.getAudioDataSize());
            clip.addLineListener(new Listener());
            //volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            released = true;
        }
        catch (LineUnavailableException e){
            e.printStackTrace();
            released = false;
            close();
        }
        this.doAfterPlaying = doAfterPlaying;
    }
    public long getMicrosecondLength(){
        return clip.getMicrosecondLength();
    }
    public String getStringLength(){
        return "%d:%d".formatted(getMicrosecondLength()/(1000000*60), getMicrosecondLength()/1000000);
    }
    public boolean init(VoiceMessage voiceMessage){
        if(released)return true;
        try{
            clip = AudioSystem.getClip();
            clip.open(voiceMessage.getAudioFormat(), voiceMessage.getAudioData(), 0, voiceMessage.getAudioDataSize());
            clip.addLineListener(new Listener());
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            released = true;
        }
        catch (LineUnavailableException e){
            e.printStackTrace();
            released = false;
            close();
        }
        return released;
    }
    // true если звук успешно загружен, false если произошла ошибка
    public boolean isReleased() {
        return released;
    }

    // проигрывается ли звук в данный момент
    public boolean isPlaying() {
        return playing;
    }

    // Запуск
	/*
	  breakOld определяет поведение, если звук уже играется
	  Если breakOld==true, о звук будет прерван и запущен заново
	  Иначе ничего не произойдёт
	*/
    public void play(boolean breakOld) {
        if (released) {
            if (breakOld) {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            } else if (!isPlaying()) {
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            }
        }
    }

    // То же самое, что и play(true)
    public void play() {
        play(true);
    }

    // Останавливает воспроизведение
    public void stop() {
        if (playing) {
            clip.stop();
        }
    }

    public void close() {
        if (clip != null)
            clip.close();
    }

    // Установка громкости
	/*
	  x долже быть в пределах от 0 до 1 (от самого тихого к самому громкому)
	*/
    public void setVolume(float x) {
        if (x<0) x = 0;
        if (x>1) x = 1;
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        volumeControl.setValue((max-min)*x+min);
    }

    // Возвращает текущую громкость (число от 0 до 1)
    public float getVolume() {
        float v = volumeControl.getValue();
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        return (v-min)/(max-min);
    }

    // Дожидается окончания проигрывания звука
    public void join() {
        if (!released) return;
        synchronized(clip) {
            try {
                while (playing)
                    clip.wait();
            } catch (InterruptedException exc) {}
        }
    }

    private class Listener implements LineListener {
        public void update(LineEvent ev) {
            if (ev.getType() == LineEvent.Type.STOP) {
                playing = false;
                synchronized(clip) {
                    clip.notify();
                    if(doAfterPlaying != null)doAfterPlaying.run();
                }
            }
        }
    }
}