package org.blbulyandavbulyan.server.commandprocessing.exceptions;

import org.blbulyandavbulyan.server.common.exceptions.ServerException;
import general.message.servercommand.ServerCommand;

public class CommandProcessingException extends ServerException {
    private ServerCommand serverCommand;
    public CommandProcessingException(ServerCommand serverCommand){
        this.serverCommand = serverCommand;
    }
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
