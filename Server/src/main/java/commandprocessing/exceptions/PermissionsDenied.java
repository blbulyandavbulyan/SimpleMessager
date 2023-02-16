package commandprocessing.exceptions;

import general.message.servercommand.ServerCommand;

public class PermissionsDenied extends CommandProcessingException{
    private ServerCommand serverCommand;
    public PermissionsDenied(ServerCommand serverCommand){
        super("You don't have permissions for command " + serverCommand.getCommandID().name());
    }
}
