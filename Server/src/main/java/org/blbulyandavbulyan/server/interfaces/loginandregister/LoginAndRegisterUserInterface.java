package org.blbulyandavbulyan.server.interfaces.loginandregister;

import org.blbulyandavbulyan.server.interfaces.loginandregister.exceptions.UserAlreadyExistsException;

public interface LoginAndRegisterUserInterface {
    boolean login(String userName, String password);
    void register(String userName, String password) throws UserAlreadyExistsException;
}
