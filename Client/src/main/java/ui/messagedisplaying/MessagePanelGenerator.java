package ui.messagedisplaying;

import general.message.Message;
import general.message.textmessage.TextMessage;
import general.message.voicemessage.VoiceMessage;
import ui.messagedisplaying.exceptions.UnknownMessageTypeException;

public class MessagePanelGenerator {
    public static MessagePanel getMessagePanel(Message msg) throws UnknownMessageTypeException{
        if(msg instanceof TextMessage){
            return new TextMessagePanel((TextMessage) msg);
        }
        else if(msg instanceof VoiceMessage){
            return new VoiceMessagePanel((VoiceMessage) msg);
        }
        else throw new UnknownMessageTypeException();
    }
}
