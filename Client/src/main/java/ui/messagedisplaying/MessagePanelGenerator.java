package ui.messagedisplaying;

import general.message.Message;
import general.message.textmessage.TextMessage;
import general.message.voicemessage.VoiceMessage;
import ui.messagedisplaying.exceptions.UnknownMessageTypeException;

import javax.sound.sampled.Mixer;
import java.util.concurrent.Callable;

public class MessagePanelGenerator {
    public MessagePanel getMessagePanel(Message msg) {
        if(msg instanceof TextMessage){
            return new TextMessagePanel((TextMessage) msg);
        }
        else if(msg instanceof VoiceMessage){
            return new VoiceMessagePanel((VoiceMessage) msg);
        }
        else throw new UnknownMessageTypeException();
    }
}
