package org.blbulyandavbulyan.server.commandprocessing.exceptions;

import general.message.servercommand.ServerCommand;

public class UserRankIsLessThanTargetRank extends PermissionsDeniedException{
    public UserRankIsLessThanTargetRank(ServerCommand serverCommand) {
        super(serverCommand);
    }
}
