package general.message.servercommand.exceptions;

public class InvalidCommandArgumentsException extends CommandException{
    private final Integer position;
    private final Class<?> requireArgType;
    public InvalidCommandArgumentsException(Integer position, Class<?> requireArgType){
        this.position = position;
        this.requireArgType = requireArgType;
    }
    public int getPosition() {
        return position;
    }

    public Class<?> getRequireArgType() {
        return requireArgType;
    }
}
