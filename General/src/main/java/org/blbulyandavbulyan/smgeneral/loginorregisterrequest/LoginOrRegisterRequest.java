package org.blbulyandavbulyan.smgeneral.loginorregisterrequest;

import java.io.Serial;
import java.io.Serializable;

public class LoginOrRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private String userName;
    private String password;
    private final OperationType operation;
    public enum OperationType {REGISTER, LOGIN, CANCELLED}

    public LoginOrRegisterRequest(String userName){
        operation = OperationType.LOGIN;
        this.userName = userName;
    }
    public LoginOrRegisterRequest(String userName, String password, OperationType operation)throws NullPointerException{
        if (operation == null) throw new NullPointerException("operation is null");
        this.operation = operation;
        if(operation != OperationType.CANCELLED) {
            if (userName == null) throw new NullPointerException("username is null");
            if (password == null) throw new NullPointerException("password is null");
            this.userName = userName;
            this.password = password;
        }
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
