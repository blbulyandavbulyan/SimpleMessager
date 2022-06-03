package user.exceptions;

public class UserNameOutOfRange extends RuntimeException{
    private String parameterName;
    public UserNameOutOfRange(String msg, String parameterName){
        super(msg);
        this.parameterName = parameterName;
    }
    public String getParameterName() {
        return parameterName;
    }
}
