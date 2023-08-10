package org.blbulyandavbulyan.smserver.commandprocessing.exceptions;

import org.blbulyandavbulyan.smserver.common.exceptions.ServerException;
import org.blbulyandavbulyan.smgeneral.message.servercommand.ServerCommand;

public class CommandProcessingException extends ServerException {
    private ServerCommand serverCommand;

    public CommandProcessingException(String msg) {
        super(msg);
    }
    public CommandProcessingException(){

    }
    public CommandProcessingException(Exception e) {
        super(e);
    }

    public ServerCommand getServerCommand() {
        return serverCommand;
    }
}
