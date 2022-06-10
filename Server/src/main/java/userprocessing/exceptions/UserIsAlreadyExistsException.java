package userprocessing.exceptions;

public class UserIsAlreadyExistsException extends RuntimeException{
    public UserIsAlreadyExistsException(String msg){
        super(msg);
    }
}
