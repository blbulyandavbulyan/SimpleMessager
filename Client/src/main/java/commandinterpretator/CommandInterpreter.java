package commandinterpretator;

import commandinterpretator.exceptions.syntax.InvalidWordsCountInCommandException;
import commandinterpretator.exceptions.syntax.InvalidCommandException;
import commandinterpretator.exceptions.syntax.SyntaxErrorException;
import general.message.servercommand.ServerCommand;

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
    *Команда grant
    * grant all privileges for user %username%
    * grant all privileges for group %groupname%
    * grant privileges DELETE_USER ADD_USER ... for user %username%
    */
    public CommandInterpreter(){

    }
    // todo add command processing
    public ServerCommand interpretCommand(String command, String executorName) throws SyntaxErrorException {
        String[] splittingCommand = command.split(" ");
        int processedArgumentNumber = 0;
        switch (splittingCommand[processedArgumentNumber]){
            case "ban", "BAN", "unban", "UNBAN" ->{
                splittingCommand[processedArgumentNumber] = splittingCommand[processedArgumentNumber].toLowerCase();
                boolean isBanCommand = splittingCommand[processedArgumentNumber].equals("ban");
                if(splittingCommand.length != 3)
                    throw new InvalidWordsCountInCommandException(3);
                switch (splittingCommand[++processedArgumentNumber]){
                    case "user", "USER", "group", "GROUP"->{
                        ServerCommand.CommandID commandID =
                                splittingCommand[processedArgumentNumber].equalsIgnoreCase("user") ? ServerCommand.CommandID.BAN_USER : ServerCommand.CommandID.BAN_GROUP;

                        String target = splittingCommand[++processedArgumentNumber];
                        return new ServerCommand(executorName, target, commandID, null);
                    }
                    default -> throw new SyntaxErrorException(processedArgumentNumber);
                }

            }
            case "delete", "DELETE"->{
                if(splittingCommand.length != 3)
                    throw new InvalidWordsCountInCommandException(3);
                switch (splittingCommand[++processedArgumentNumber]){
                    case "user", "USER", "group", "GROUP"->{
                        ServerCommand.CommandID commandID =
                                splittingCommand[processedArgumentNumber].equalsIgnoreCase("user") ? ServerCommand.CommandID.DELETE_USER : ServerCommand.CommandID.DELETE_GROUP;

                        String target = splittingCommand[++processedArgumentNumber];
                        return new ServerCommand(executorName, target, commandID, null);
                    }
                    default -> throw new SyntaxErrorException(processedArgumentNumber);
                }
            }
            /*
            * change user name for %username% on %newusername% - изменяет имя пользователя с именем %username% на %newusername%
            * change user password for %username% on %newpassword% - изменяет пароль пользователя с именем %username% на %newpassword%
            * change user rank for %username% on %newuserrank% - изменяет ранг пользователя с именем %username% на %newuserrank%
            */
            case "change", "CHANGE"->{
                if(splittingCommand.length < 3)
                    throw new InvalidWordsCountInCommandException(3);
                switch (splittingCommand[++processedArgumentNumber]){
                    case "user", "USER"->{
                        if(splittingCommand.length != 7)
                            throw new InvalidWordsCountInCommandException(7);
                        switch (splittingCommand[++processedArgumentNumber]){
                            case "name", "NAME", "password", "PASSWORD"->{
                                ServerCommand.CommandID commandID =
                                        splittingCommand[processedArgumentNumber].equalsIgnoreCase("name") ? ServerCommand.CommandID.RENAME_USER : ServerCommand.CommandID.CHANGE_PASSWORD;
                                if(splittingCommand[++processedArgumentNumber].equalsIgnoreCase("for")){
                                    String targetUserName = splittingCommand[++processedArgumentNumber];
                                    if(splittingCommand[++processedArgumentNumber].equalsIgnoreCase("on"))
                                        throw new SyntaxErrorException(--processedArgumentNumber);
                                    return new ServerCommand(executorName, targetUserName, commandID, splittingCommand[++processedArgumentNumber]);
                                }
                                else throw new SyntaxErrorException(--processedArgumentNumber);

                            }
                        }
                    }
                    case "password", "PASSWORD"->{
                        if(splittingCommand.length != 3)
                            throw new InvalidWordsCountInCommandException(3);
                        return new ServerCommand(executorName, executorName, ServerCommand.CommandID.CHANGE_PASSWORD, splittingCommand[++processedArgumentNumber]);
                    }
                    default -> throw new SyntaxErrorException(processedArgumentNumber);
                }

            }
            case "grant", "GRANT"->{
                switch (splittingCommand[++processedArgumentNumber]){
                    case "all", "ALL" ->{
                        if(splittingCommand[++processedArgumentNumber].equalsIgnoreCase("privileges")){

                        }
                        else throw new SyntaxErrorException(--processedArgumentNumber);
                    }
                    case "privileges", "PRIVILEGES" ->{

                    }
                    default -> throw new SyntaxErrorException(processedArgumentNumber);
                }
            }
            default -> throw new SyntaxErrorException(processedArgumentNumber);
        }
        return null;
    }
}
