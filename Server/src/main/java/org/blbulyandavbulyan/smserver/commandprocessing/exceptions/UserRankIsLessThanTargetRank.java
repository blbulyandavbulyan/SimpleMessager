package org.blbulyandavbulyan.smserver.commandprocessing.exceptions;

import org.blbulyandavbulyan.smgeneral.message.servercommand.ServerCommand;

public class UserRankIsLessThanTargetRank extends PermissionsDeniedException{
    public UserRankIsLessThanTargetRank(ServerCommand serverCommand) {
        super(serverCommand);
    }
}
