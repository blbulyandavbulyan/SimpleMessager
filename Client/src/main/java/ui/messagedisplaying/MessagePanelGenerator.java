package ui.messagedisplaying;

import general.message.Message;
import general.message.textmessage.TextMessage;
import general.message.voicemessage.VoiceMessage;
import ui.messagedisplaying.exceptions.UnknownMessageTypeException;
import ui.messagedisplaying.messagepanels.MessagePanel;
import ui.messagedisplaying.messagepanels.TextMessagePanel;
import ui.messagedisplaying.messagepanels.VoiceMessagePanel;

public class MessagePanelGenerator {
    public static MessagePanel getMessagePanel(Message msg) {
        if(msg instanceof TextMessage){
            return new TextMessagePanel((TextMessage) msg);
        }
        else if(msg instanceof VoiceMessage){
            return new VoiceMessagePanel((VoiceMessage) msg);
        }
        else throw new UnknownMessageTypeException();
    }
}
