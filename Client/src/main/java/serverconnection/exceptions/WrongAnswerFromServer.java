package serverconnection.exceptions;

public class WrongAnswerFromServer extends RuntimeException{
    public WrongAnswerFromServer(String msg){
        super(msg);
    }
}
