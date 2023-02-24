package commandprocessing;

import commandprocessing.exceptions.CommandProcessingException;
import commandprocessing.exceptions.ExecutionError;
import commandprocessing.exceptions.PermissionsDeniedException;
import commandprocessing.exceptions.UserRankIsLessThanTargetRank;
import general.message.servercommand.ServerCommand;
import manager.ManagerInterface;
import manager.groupprocessing.GroupManager;
import manager.userprocessing.User;
import manager.userprocessing.UserManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommandProcessor {
    private UserManager userManager;
    private GroupManager groupManager;
    private final HashMap<ServerCommand.TargetType, ManagerInterface> targetTypeToManagerInterfaceMapper;
    //эта переменная хранит экземпляр класса User для исполнителя команды, делается это для ускоренной проверки привилегий
    private final User executor;
    public CommandProcessor(UserManager userManager, GroupManager groupManager, User executor){
        this.executor = executor;
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
        //обязательная самопроверка команды на корректность
        serverCommand.selfCheck();
        //базовая проверка привилегий, это позволит проверить может ли пользователь в принципе выполнять подобную команду
        if(!canUserExecuteThisCommand(serverCommand))
            throw new PermissionsDeniedException(serverCommand);
        ManagerInterface managerInterface = targetTypeToManagerInterfaceMapper.get(serverCommand.getTargetType());
        ServerCommand.Command command = serverCommand.getCommand();
        switch (command){
            //команда add закомментирована, поскольку есть небольшая сложность в её реализации её не слишком возможно унифицировать
            case ADD -> {

            }
            case BAN, UNBAN, DELETE -> {
                //это универсальный обработчик для команд BAN, UNBAN, DELETE
                //он так же потенциально может подойти для любой команды, функция исполнитель которой возвращает void и принимает имя цели
                Consumer<String> commandFunction = switch (command){
                    case BAN ->  managerInterface::ban;
                    case UNBAN -> managerInterface::unban;
                    case DELETE -> managerInterface::delete;
                    default -> throw new CommandProcessingException("impossible exception !!!");
                };
                Object target = serverCommand.getTarget();
                String[] targetNames = null;
                if(target instanceof String){
                    targetNames = new String[1];
                    targetNames[0] = (String) target;
                }
                else if(target instanceof String[]){
                    targetNames = (String[]) target;
                }
                for (String targetName : targetNames){
                    if(executor.getRank() > managerInterface.getRank(targetName)){
                        //если ранг исполнителя больше, то мы можем исполнять эту команду
                        commandFunction.accept(targetName);
                    }
                    else throw new UserRankIsLessThanTargetRank(serverCommand);
                }
            }
//            case RENAME, CHANGE_PASSWORD -> {
//                BiConsumer<String, String> commandFunction = command == ServerCommand.Command.RENAME ? managerInterface::rename : userManager::changePassword;
//
//            }
            case RENAME -> {
                String targetName = (String) serverCommand.getTarget();
                if(executor.getRank() > managerInterface.getRank(targetName))
                    managerInterface.rename(targetName, (String) serverCommand.getArgument());
            }
            case CHANGE_PASSWORD -> {
                String targetName = (String) serverCommand.getTarget();
                if(serverCommand.getTargetType() != ServerCommand.TargetType.EXECUTOR){
                    if(executor.getRank() > managerInterface.getRank(targetName))
                        userManager.changePassword(targetName, (String) serverCommand.getArgument());
                }
                else userManager.changePassword((String) serverCommand.getTarget(), (String) serverCommand.getArgument());
            }
        }
        return null;
    }
    //данный метод будет обрабатывать команды, которые могут выполнится либо успешно, либо нет, в зависимости от того, есть ли у пользователя требуемые права на её выполнения
    //при этом, ни каких выходных данных от него не ожидается
    //если выполнится успешно - значит не будет исключения
    //если выполнится не успешно, значит будет исключение
    private void processCommandsWithSingleResult(ServerCommand serverCommand) throws SQLException {
//
    }
    private boolean canUserExecuteThisCommand(ServerCommand serverCommand){
        //быстрая проверка на право выполнения команды CHANGE_PASSWORD если она направлена на исполнителя:
        if(serverCommand.getTargetType() == ServerCommand.TargetType.EXECUTOR && serverCommand.getCommand() == ServerCommand.Command.CHANGE_PASSWORD)
            return true;
        else {
            return executor.canExecute(serverCommand.getCommand()) && executor.allowedTargetType(serverCommand.getTargetType());
        }
    }

}
