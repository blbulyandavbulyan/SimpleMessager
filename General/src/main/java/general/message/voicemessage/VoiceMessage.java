package general.message.voicemessage;

import general.message.Message;
import general.message.voicemessage.exceptions.AudioDataIsEmptyException;
import general.message.voicemessage.exceptions.AudioDataIsNullException;
import general.message.voicemessage.exceptions.AudioFormatIsNullException;

import javax.sound.sampled.AudioFormat;
import java.io.Serial;
import java.io.Serializable;

public class VoiceMessage  extends Message implements Serializable {
    @Serial
    private static final long serialVersionUID = -3391271502340731799L;
    private final byte []audioData;
    private final AudioFormat.Encoding encoding;
    private final float sampleRate;
    private final int sampleSizeInBits;
    private final int channels;
    private final int frameSize;
    private final float frameRate;
    private final boolean bigEndian;
   // private final int lenghtInSeconds;
    private AudioFormat audioFormat = null;
    public VoiceMessage(String sender, String receiver, byte []audioData, AudioFormat audioFormat){
        super(sender, receiver);
        if(audioData == null)throw new AudioDataIsNullException();
        if(audioData.length == 0)throw new AudioDataIsEmptyException();
        if(audioFormat == null)throw new AudioFormatIsNullException();
        encoding = audioFormat.getEncoding();
        sampleRate = audioFormat.getSampleRate();
        sampleSizeInBits = audioFormat.getSampleSizeInBits();
        channels = audioFormat.getChannels();
        frameSize = audioFormat.getFrameSize();
        frameRate = audioFormat.getFrameRate();
        bigEndian = audioFormat.isBigEndian();
        this.audioData = audioData;
    }
    public AudioFormat getAudioFormat(){
        if(audioFormat == null)audioFormat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
        return audioFormat;
    }
    public byte[] getAudioData() {
        return audioData;
    }
    public int getAudioDataSize(){
        return audioData.length;
    }
}
