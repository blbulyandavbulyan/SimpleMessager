package loginandregister;

import loginandregister.exceptions.UserAlreadyExistsException;

public interface LoginAndRegisterUserInterface {
    boolean login(String userName, String password);
    void register(String userName, String password) throws UserAlreadyExistsException;
}
