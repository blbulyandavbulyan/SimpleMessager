package org.blbulyandavbulyan.smgeneral.message.textmessage;

import org.blbulyandavbulyan.smgeneral.message.Message;
import org.blbulyandavbulyan.smgeneral.message.textmessage.exceptions.MessageStringIsEmptyException;

import java.io.Serial;
import java.io.Serializable;

public class TextMessage extends Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 6154037109176482643L;
    protected final String message;
    public TextMessage(String message, String sender, String receiver){
        super(sender, receiver);
        this.message = message;
        correctOrThrow();

    }
    public String getMessageString(){
        return message;
    }
    public void correctOrThrow(){
        if(message == null || message.isEmpty()|| message.isBlank())throw new MessageStringIsEmptyException();

    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getSender());
        sb.append(": ");
        sb.append(getSendingTimeString());
        sb.append(" > ");
        sb.append(message);
        return sb.toString();
    }

}
