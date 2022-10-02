package ui.components.displayers.generators;

import general.message.Message;
import general.message.filemessages.ImageFileMessage;
import general.message.textmessage.TextMessage;
import general.message.voicemessage.VoiceMessage;
import ui.components.displayers.messagedisplaying.messagepanels.MessagePanel;
import ui.components.displayers.messagedisplaying.messagepanels.filemessagespanels.ImageFileMessagePanel;
import ui.components.displayers.messagedisplaying.messagepanels.standartmessagepanels.TextMessagePanel;
import ui.components.displayers.messagedisplaying.messagepanels.standartmessagepanels.VoiceMessagePanel;
import ui.components.displayers.messagedisplaying.exceptions.UnknownMessageTypeException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessagePanelGenerator {
    private final ResourceBundle rb;
    private static final Map<Class<? extends Message>, Class<? extends MessagePanel>> messageMapper = new HashMap<>();
    static {
        messageMapper.put(VoiceMessage.class, VoiceMessagePanel.class);
        messageMapper.put(TextMessage.class, TextMessagePanel.class);
        messageMapper.put(ImageFileMessage.class, ImageFileMessagePanel.class);
    }
    public MessagePanelGenerator(ResourceBundle rb){
        if(rb == null)throw new NullPointerException();
        if(!rb.containsKey("voiceMessagePanel.Play"))
            throw new MissingResourceException("Error creation MessagePanelGenerator", rb.getBaseBundleName() , "voiceMessagePanel.Play");
        this.rb = rb;
    }
    public MessagePanel getMessagePanel(Message msg) {
        if(messageMapper.containsKey(msg.getClass())){
            try {
                return messageMapper.get(msg.getClass()).getConstructor(msg.getClass(), ResourceBundle.class).newInstance(msg, rb);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        else throw new UnknownMessageTypeException();
    }
}
