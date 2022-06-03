package ui;
public class LoginOrRegisterResult {
    private String userName;
    private String password;
    private final OperationType operation;
    enum OperationType {REGISTER, LOGIN, CANCELLED}

    public LoginOrRegisterResult(String userName){
        operation = OperationType.LOGIN;
        this.userName = userName;
    }
    public LoginOrRegisterResult(String userName, String password, OperationType operation){
        if(operation == null)throw new NullPointerException("operation is null");
        if(userName == null)throw new NullPointerException("username is null");
        if(password == null)throw new NullPointerException("password is null");
        this.operation = operation;
        this.userName = userName;
        this.password = password;
    }
    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public OperationType getOperation() {
        return operation;
    }
}
