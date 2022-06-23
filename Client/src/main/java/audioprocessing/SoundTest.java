package audioprocessing;

import javax.sound.sampled.*;

public class SoundTest {

    public static void main(String[] args) {

        System.out.println("Mixers ...");

        Mixer.Info[] mixers = AudioSystem.getMixerInfo();

        for (Mixer.Info mixer : mixers) {
            System.out.println("Mixers: " + mixer);
        }
        Line.Info[] lineInfos = AudioSystem.getTargetLineInfo
            (new Line.Info(TargetDataLine.class));

        AudioFormat nativeFormat = null;
   
        // find a usable target line
        for (Line.Info lineInfo : lineInfos) {

            System.out.println("Line info: " + lineInfo);
            AudioFormat[] formats =
                    ((TargetDataLine.Info) lineInfo).getFormats();

            for (AudioFormat format : formats) {
                System.out.println("   format " + format);
            }
        }
        System.exit(0);
    }
}