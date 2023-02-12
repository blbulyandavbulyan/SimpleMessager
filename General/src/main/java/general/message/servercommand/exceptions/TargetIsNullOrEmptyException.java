package general.message.servercommand.exceptions;

public class TargetIsNullOrEmptyException extends CommandException{
    private final Integer positionInArray;

    public TargetIsNullOrEmptyException(){
        this(null);
    }
    public TargetIsNullOrEmptyException(Integer positionInArray){
        this.positionInArray = positionInArray;
    }
}
