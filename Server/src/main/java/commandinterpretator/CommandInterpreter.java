package commandinterpretator;

import commandinterpretator.exceptions.permissions.PermissionsDeniedException;
import commandinterpretator.exceptions.permissions.TargetUserHasRankMoreThanExecutorException;
import commandinterpretator.exceptions.syntax.InvalidArgumentsCountException;
import commandinterpretator.exceptions.syntax.InvalidCommandException;
import commandinterpretator.exceptions.syntax.SyntaxErrorException;
import groupprocessing.GroupManager;
import userprocessing.Privilege;
import userprocessing.User;
import userprocessing.UserManager;

import java.sql.SQLException;

public class CommandInterpreter {
    /*
    * Доступные команды:
    * Команда change:
    * change user name for %username% on %newusername% - изменяет имя пользователя с именем %username% на %newusername%
    * change user password for %username% on %newpassword% - изменяет пароль пользователя с именем %username% на %newpassword%
    * change user rank for %username% on %newuserrank% - изменяет ранг пользователя с именем %username% на %newuserrank%
    * Команды блокировки и разблокировки, отключения пользователя от сервера:
    * ban user %username% - блокирует аккаунт пользователя с именем %username%
    * unban user %username% - разблокирует аккаунт пользователя с именем %username%
    * kick %username% - заставляет сервер разорвать соединение с пользователем
    * Команда delete:
    * delete user %username% - удаляет аккаунт пользователя с именем %username%
    * delete user %username% from group %groupname% - удаляет пользователя с именем %username% из группы с именем %groupname%
    * Команда add:
    * add user %username% %password% - добавляет пользователя с именем %username% и паролем %password%
    * add group %groupname% - добавляет группу с именем %groupname%
    * add user %username% to group %groupname% - добавляет пользователя с именем %username% в группу с именем %groupname%
    *
    *
    */
    private final UserManager userManager;
    private final GroupManager groupManager;
    public CommandInterpreter(UserManager userManager, GroupManager groupManager){
        if(userManager == null)
            throw new NullPointerException("userManager is null!");
        if(groupManager == null)
            throw new NullPointerException("groupManager is null!");
        this.userManager = userManager;
        this.groupManager = groupManager;
    }
    // todo add command processing
    public void interpretCommand(String command, User executor, boolean checkPrivileges, boolean checkRank) throws SQLException, PermissionsDeniedException, SyntaxErrorException {
        String[] splittingCommand = command.split(" ");
        switch (splittingCommand[0]){
            case "ban", "BAN", "unban", "UNBAN" ->{
                splittingCommand[0] = splittingCommand[0].toLowerCase();
                boolean isBanCommand = splittingCommand[0].equals("ban");
                if(splittingCommand.length != 3)
                    throw new InvalidArgumentsCountException(3);
                switch (splittingCommand[1]){
                    case "user", "USER"->{
                        if(!checkPrivileges || executor.hasPrivelege(isBanCommand ? Privilege.BAN_USER : Privilege.UNBAN_USER)){
                            if (!checkRank || executor.getRank() > userManager.getUserRank(splittingCommand[2])) {
                                if (isBanCommand) userManager.banUser(splittingCommand[2]);
                                else userManager.unbanUser(splittingCommand[2]);
                            }
                            throw new TargetUserHasRankMoreThanExecutorException();
                        }
                        else throw new PermissionsDeniedException();
                    }
                    case "group", "GROUP" ->{
                        if(!checkPrivileges || executor.hasPrivelege(isBanCommand ? Privilege.BAN_GROUP : Privilege.UNBAN_GROUP)){
                            if (!checkRank || executor.getRank() > groupManager.getGroupRank(splittingCommand[2])) {
                                if(isBanCommand)groupManager.banGroup(splittingCommand[2]);
                                else groupManager.unbanGroup(splittingCommand[2]);
                            }
                            throw new TargetUserHasRankMoreThanExecutorException();
                        }
                        else throw new PermissionsDeniedException();
                    }
                    default -> throw new SyntaxErrorException();
                }

            }
            case "delete", "DELETE"->{

                if(splittingCommand.length != 3)
                    throw new InvalidArgumentsCountException(3);
                switch (splittingCommand[1]){
                    case "user", "USER"->{
                        if(!checkPrivileges || executor.hasPrivelege(Privilege.DELETE_USER)){

                        }
                    }
                    case "group", "GROUP" ->{
                        if(!checkPrivileges || executor.hasPrivelege(Privilege.DELETE_GROUP)){

                        }
                    }
                    default -> throw new SyntaxErrorException();
                }
            }
            case "change", "CHANGE"->{
                switch (splittingCommand[1]){
                    case "user", "USER"->{
                        switch (splittingCommand[2]){
                            case "name", "NAME"->{

                            }
                            case "password", "PASSWORD"->{

                            }
                        }
                    }
                    default -> throw new SyntaxErrorException();
                }
            }
            case "grant", "GRANT"->{

            }
            default -> throw new InvalidCommandException();
        }
    }
}
