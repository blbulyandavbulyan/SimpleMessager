package ui.messagedisplaying;

import general.message.Message;
import general.message.textmessage.TextMessage;
import general.message.voicemessage.VoiceMessage;
import ui.messagedisplaying.exceptions.UnknownMessageTypeException;

import javax.sound.sampled.Mixer;
import java.util.concurrent.Callable;

public class MessagePanelGenerator {
    Callable<Mixer> mixerGetter;
    public MessagePanelGenerator(Callable<Mixer> mixerGetter){
        this.mixerGetter = mixerGetter;
    }
    public MessagePanel getMessagePanel(Message msg) throws Exception {
        if(msg instanceof TextMessage){
            return new TextMessagePanel((TextMessage) msg);
        }
        else if(msg instanceof VoiceMessage){
            return new VoiceMessagePanel((VoiceMessage) msg, mixerGetter.call());
        }
        else throw new UnknownMessageTypeException();
    }
}
