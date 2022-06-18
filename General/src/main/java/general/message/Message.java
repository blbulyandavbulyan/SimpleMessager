package general.message;

import general.message.exceptions.ReceiverIsEmptyException;
import general.message.exceptions.SenderIsEmptyException;
import general.message.exceptions.SenderIsNullException;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = -228921989210272701L;
    protected final String sender;
    protected final String receiver;
    private final long epochMilli;
    protected Message(String sender, String receiver){
        if(sender == null)throw new SenderIsNullException();
        if(sender.isEmpty() || sender.isBlank())throw new SenderIsEmptyException();
        if(receiver != null && (receiver.isEmpty() || receiver.isBlank()))throw new ReceiverIsEmptyException();
        this.sender = sender;
        this.receiver = receiver;
        epochMilli = Instant.now().toEpochMilli();
    }
    public String getReceiver() {
        return receiver;
    }
    public String getSender(){
        return sender;
    }
    public String getSendingTimeString(){
        ZoneId myZone = ZoneId.systemDefault();
        ZonedDateTime nowInMyZone = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), myZone);
        return nowInMyZone.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}
