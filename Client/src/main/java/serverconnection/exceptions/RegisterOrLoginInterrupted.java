package serverconnection.exceptions;

public class RegisterOrLoginInterrupted extends RuntimeException{
    public RegisterOrLoginInterrupted(String msg){
        super(msg);
    }
}
