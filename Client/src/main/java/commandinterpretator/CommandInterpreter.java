package commandinterpretator;

import commandinterpretator.exceptions.permissions.PermissionsDeniedException;
import commandinterpretator.exceptions.permissions.TargetGroupHasRankMoreThanExecutor;
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
        int processedArgumentNumber = 0;
        switch (splittingCommand[processedArgumentNumber]){
            case "ban", "BAN", "unban", "UNBAN" ->{
                splittingCommand[processedArgumentNumber] = splittingCommand[processedArgumentNumber].toLowerCase();
                boolean isBanCommand = splittingCommand[processedArgumentNumber].equals("ban");
                if(splittingCommand.length != 3)
                    throw new InvalidArgumentsCountException(3);
                switch (splittingCommand[++processedArgumentNumber]){
                    case "user", "USER"->{
                        if(!checkPrivileges || executor.hasPrivilege(isBanCommand ? Privilege.BAN_USER : Privilege.UNBAN_USER)){
                            String targetUserName = splittingCommand[++processedArgumentNumber];
                            if (!checkRank || executor.getRank() > userManager.getUserRank(targetUserName)) {
                                if (isBanCommand) userManager.banUser(targetUserName);
                                else userManager.unbanUser(targetUserName);
                            }
                            throw new TargetUserHasRankMoreThanExecutorException();
                        }
                        else throw new PermissionsDeniedException();
                    }
                    case "group", "GROUP" ->{
                        if(!checkPrivileges || executor.hasPrivilege(isBanCommand ? Privilege.BAN_GROUP : Privilege.UNBAN_GROUP)){
                            String targetGroupName = splittingCommand[++processedArgumentNumber];
                            if (!checkRank || executor.getRank() > groupManager.getGroupRank(targetGroupName)) {
                                if(isBanCommand)groupManager.banGroup(targetGroupName);
                                else groupManager.unbanGroup(targetGroupName);
                            }
                            throw new TargetGroupHasRankMoreThanExecutor();
                        }
                        else throw new PermissionsDeniedException();
                    }
                    default -> throw new SyntaxErrorException();
                }

            }
            case "delete", "DELETE"->{

                if(splittingCommand.length != 3)
                    throw new InvalidArgumentsCountException(3);
                switch (splittingCommand[++processedArgumentNumber]){
                    case "user", "USER"->{
                        if(!checkPrivileges || executor.hasPrivilege(Privilege.DELETE_USER)){
                            String targetUserName = splittingCommand[++processedArgumentNumber];
                            if (!checkRank || executor.getRank() > userManager.getUserRank(targetUserName)) {
                                userManager.removeUser(targetUserName);
                            }
                            throw new TargetUserHasRankMoreThanExecutorException();
                        }
                        else throw new PermissionsDeniedException();
                    }
                    case "group", "GROUP" ->{
                        if(!checkPrivileges || executor.hasPrivilege(Privilege.DELETE_GROUP)){
                            String targetGroupName = splittingCommand[++processedArgumentNumber];
                            if (!checkRank || executor.getRank() > groupManager.getGroupRank(targetGroupName)) {
                                groupManager.removeGroup(targetGroupName);
                            }
                            throw new TargetGroupHasRankMoreThanExecutor();
                        }
                        else throw new PermissionsDeniedException();
                    }
                    default -> throw new SyntaxErrorException();
                }
            }
            /*
            * change user name for %username% on %newusername% - изменяет имя пользователя с именем %username% на %newusername%
            * change user password for %username% on %newpassword% - изменяет пароль пользователя с именем %username% на %newpassword%
            * change user rank for %username% on %newuserrank% - изменяет ранг пользователя с именем %username% на %newuserrank%
            */
            case "change", "CHANGE"->{
                if(splittingCommand.length < 3)
                    throw new InvalidArgumentsCountException(3);
                switch (splittingCommand[++processedArgumentNumber]){
                    case "user", "USER"->{
                        if(splittingCommand.length != 7)
                            throw new InvalidArgumentsCountException(7);
                        switch (splittingCommand[++processedArgumentNumber]){
                            case "name", "NAME"->{

                            }
                            case "password", "PASSWORD"->{
                                if(splittingCommand[++processedArgumentNumber].equalsIgnoreCase("for")){
                                    String targetUserName = splittingCommand[++processedArgumentNumber];
                                    if(splittingCommand[++processedArgumentNumber].equalsIgnoreCase("on"))
                                        throw new SyntaxErrorException();
                                    if(executor.getUserName().equals(targetUserName) ||
                                            !checkPrivileges ||
                                            (
                                                    executor.hasPrivilege(Privilege.CHANGE_PASSWORD_FOR_ANOTHER_USER) &&
                                                    (!checkRank || executor.getRank() > userManager.getUserRank(targetUserName))
                                            )
                                    ) userManager.changePassword(targetUserName, splittingCommand[++processedArgumentNumber]);
                                    else throw new PermissionsDeniedException();
                                }
                                else throw new SyntaxErrorException();

                            }
                        }
                    }
                    case "password", "PASSWORD"->{
                        if(splittingCommand.length != 3)
                            throw new InvalidArgumentsCountException(3);
                        userManager.changePassword(executor.getUserName(), splittingCommand[++processedArgumentNumber]);
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
