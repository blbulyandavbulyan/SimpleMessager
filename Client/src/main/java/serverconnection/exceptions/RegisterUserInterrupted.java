package serverconnection.exceptions;

public class RegisterUserInterrupted extends RuntimeException{
    public RegisterUserInterrupted(String msg){
        super(msg);
    }
}
