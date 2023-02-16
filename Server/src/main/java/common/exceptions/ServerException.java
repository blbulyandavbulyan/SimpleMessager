package common.exceptions;

public class ServerException extends RuntimeException{
    public ServerException(){

    }
    public ServerException(String msg){
        super(msg);
    }
}
