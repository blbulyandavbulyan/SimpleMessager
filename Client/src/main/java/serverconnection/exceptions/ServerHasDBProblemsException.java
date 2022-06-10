package serverconnection.exceptions;

public class ServerHasDBProblemsException extends RuntimeException{
    public ServerHasDBProblemsException(String msg){
        super(msg);
    }
}
