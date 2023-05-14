package org.blbulyandavbulyan.smgeneral.message.servermessages;

import java.io.Serial;

public class ServerErrorMessage extends ServerMessage{
    @Serial
    private static final long serialVersionUID = 4688715509006017188L;

    public enum ServerErrorCode {
        MESSAGE_DELIVERY_ERROR_NO_USER_WITH_THIS_NAME,
        YOUR_USERNAME_ON_SERVER_AND_IN_YOUR_MESSAGE_ARE_NOT_EQUAL,
        PERMISSIONS_DENIED,
        NO_TARGET_USERNAME,
        NO_TARGET_GROUP
    }
    private final ServerErrorCode serverErrorCode;
    private final String additionalInformationAboutError;
    public ServerErrorMessage(String receiver, String additionalInformationAboutError, ServerErrorCode serverErrorCode) {
        super(receiver);
        this.serverErrorCode = serverErrorCode;
        this.additionalInformationAboutError = additionalInformationAboutError;
    }
    public ServerErrorCode getServerErrorCode() {
        return serverErrorCode;
    }

    public String getAdditionalInformationAboutError() {
        return additionalInformationAboutError;
    }
}
