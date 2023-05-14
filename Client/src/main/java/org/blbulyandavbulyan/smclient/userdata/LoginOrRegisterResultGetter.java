package org.blbulyandavbulyan.smclient.userdata;

import general.loginorregisterrequest.LoginOrRegisterRequest;
import org.blbulyandavbulyan.smclient.serverconnection.interfaces.StatusMessagePrinter;

import java.io.Closeable;
public interface LoginOrRegisterResultGetter extends StatusMessagePrinter, Closeable {
    enum ActionCode{GET, GET_BECAUSE_INVALID_LOGIN_OR_PASSWORD, GET_NEW_USERNAME_BECAUSE_OLD_IS_ALREADY_REGISTERED}
    LoginOrRegisterRequest getLoginOrRegisterResult(ActionCode actionCode);
}
