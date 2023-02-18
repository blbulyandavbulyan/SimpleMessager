package commandprocessing;

import commandprocessing.exceptions.PermissionsDenied;
import general.message.servercommand.ServerCommand;
import manager.ManagerInterface;
import manager.groupprocessing.GroupManager;
import manager.userprocessing.UserManager;

import java.sql.SQLException;
import java.util.HashMap;

public class CommandProcessor {
    private UserManager userManager;
    private GroupManager groupManager;
    private final HashMap<ServerCommand.TargetType, ManagerInterface> targetTypeToManagerInterfaceMapper;
    public CommandProcessor(UserManager userManager, GroupManager groupManager){
        this.userManager = userManager;
        this.groupManager = groupManager;
        targetTypeToManagerInterfaceMapper = new HashMap<>();
        //не универсальное решение, что если будет больше Manager ?
        targetTypeToManagerInterfaceMapper.put(ServerCommand.TargetType.USER, userManager);
        targetTypeToManagerInterfaceMapper.put(ServerCommand.TargetType.USERS, userManager);
        targetTypeToManagerInterfaceMapper.put(ServerCommand.TargetType.GROUP, groupManager);
        targetTypeToManagerInterfaceMapper.put(ServerCommand.TargetType.GROUPS, groupManager);
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
//
    }
    private boolean canUserDoIt(ServerCommand serverCommand){
        //fixme
        return false;
    }

}
