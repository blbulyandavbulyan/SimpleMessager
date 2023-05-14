package org.blbulyandavbulyan.smclient.processings.audioprocessing.play;

import general.message.voicemessage.VoiceMessage;

public class PlayVoiceMessage extends PlayAudio {
    public PlayVoiceMessage(VoiceMessage voiceMessage, Runnable doAfterPlaying){
        //System.out.printf("Размер аудио сообщения %d\n", voiceMessage.getAudioDataSize());
        super(voiceMessage.getAudioData(), voiceMessage.getAudioFormat(), doAfterPlaying);
    }
    public String getFormattedAudioMessageLength(){
        return "%d:%d".formatted(getMicrosecondLength()/(1000000*60), getMicrosecondLength()/1000000);
    }
}