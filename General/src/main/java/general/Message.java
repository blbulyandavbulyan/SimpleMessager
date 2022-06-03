package general;

import messageExceptions.EmptyMessageException;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String message;
    private String sender;
    private String receiver;
    Instant nowUTC;
    public Message(String message, String sender, String receiver){
        this.message = message.trim();
        this.sender = sender;
        this.receiver = receiver;
        correctOrThrow();
        nowUTC = Instant.now();
    }
    public void correctOrThrow(){
        if(message == null || message.length() == 0 )throw new EmptyMessageException("Вы ввели пустое сообщение!");

    }
    @Override
    public String toString() {
        ZoneId myZone = ZoneId.systemDefault();
        ZonedDateTime nowInMyZone = ZonedDateTime.ofInstant(nowUTC, myZone);
        StringBuilder sb = new StringBuilder(sender);
        sb.append(": ");
        sb.append(nowInMyZone.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm > ")));
        sb.append(message);
        return sb.toString();
    }
    public String getReceiver() {
        return receiver;
    }
    public String getSender(){
        return sender;
    }
}
