package commandprocessing;

import commandprocessing.exceptions.PermissionsDenied;
import general.message.servercommand.ServerCommand;
import groupprocessing.GroupManager;
import userprocessing.UserManager;

import java.sql.SQLException;

public class CommandProcessor {
    private UserManager userManager;
    private GroupManager groupManager;
    public CommandProcessor(UserManager userManager, GroupManager groupManager){
        this.userManager = userManager;
        this.groupManager = groupManager;
    }
    public Object processCommand(ServerCommand serverCommand){
        if(!canUserDoIt(serverCommand))
            throw new PermissionsDenied(serverCommand);
        //fixme
        return null;
    }
    //данный метод будет обрабатывать команды, которые могут выполнится либо успешно, либо нет, в зависимости от того, есть ли у пользователя требуемые права на её выполнения
    //при этом, ни каких выходных данных от него не ожидается
    //если выполнится успешно - значит не будет исключения
    //если выполнится не успешно, значит будет исключение
    private void processCommandsWithSingleResult(ServerCommand serverCommand) throws SQLException {

        switch (serverCommand.getCommandID()){
            case ADD_USER -> {
                if(!userManager.userIsExist((String) serverCommand.getTarget())){

                }
            }
            case DELETE_USER -> {

            }
        }
    }
    private boolean canUserDoIt(ServerCommand serverCommand){
        //fixme
        return false;
    }
}
