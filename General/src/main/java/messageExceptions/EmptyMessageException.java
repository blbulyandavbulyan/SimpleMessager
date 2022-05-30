package messageExceptions;

public class EmptyMessageException extends RuntimeException{
    public EmptyMessageException(String msg){
        super(msg);
    }
}
