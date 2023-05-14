package org.blbulyandavbulyan.server.commandprocessing.exceptions;

import general.message.servercommand.ServerCommand;

public class PermissionsDeniedException extends CommandProcessingException{
    private final ServerCommand serverCommand;
    public PermissionsDeniedException(ServerCommand serverCommand){
        super("You don't have permissions for command " + serverCommand.getCommand().name());
        this.serverCommand = serverCommand;
    }

    public ServerCommand getServerCommand() {
        return serverCommand;
    }
}
