package general.message.servercommand;

import general.message.Message;
import general.message.servercommand.exceptions.ArgumentIsNullException;
import general.message.servercommand.exceptions.InvalidCommandArgumentsException;
import general.message.servercommand.exceptions.TargetIsNullOrEmptyException;

import java.io.Serial;

public class ServerCommand extends Message {
    @Serial
    private static final long serialVersionUID = -6937887162749222729L;
    public enum CommandID{
        BAN_USER,
        BAN_GROUP,
        ADD_USER,
        ADD_GROUP,
        DELETE_USER,
        DELETE_USERS,

        DELETE_GROUP,
        DELETE_GROUPS,
        RENAME_USER,
        RENAME_GROUP,
        CHANGE_PASSWORD,
        CHANGE_USER_RANK,
        CHANGE_GROUP_RANK

    }
    private final Object argument;
    private final Object target;
    private final CommandID commandID;
    public void selfCheck(){
        switch (commandID){
            //single targets command check
            case ADD_USER, DELETE_USER, ADD_GROUP, DELETE_GROUP, BAN_USER, BAN_GROUP->{
                if(target != null && !(target instanceof String))throw new InvalidCommandArgumentsException(null, String.class);
                if(target == null ||  ((String)target).isBlank()) throw new TargetIsNullOrEmptyException();

            }
            case CHANGE_GROUP_RANK, CHANGE_USER_RANK->{
                if(target != null && !(target instanceof String))throw new InvalidCommandArgumentsException(null, String.class);
                if(target == null ||  ((String)target).isBlank()) throw new TargetIsNullOrEmptyException();
                if(!(argument instanceof Integer))throw new InvalidCommandArgumentsException(null, Integer.class);
            }
            case RENAME_GROUP, RENAME_USER, CHANGE_PASSWORD -> {
                if(target != null && !(target instanceof String))throw new InvalidCommandArgumentsException(null, String.class);
                if(target == null ||  ((String)target).isBlank()) throw new TargetIsNullOrEmptyException();
                if(argument == null)throw new ArgumentIsNullException();
                if(!(argument instanceof String))throw new InvalidCommandArgumentsException(null, String.class);

            }
            case DELETE_USERS, DELETE_GROUPS ->{
                if(target == null) throw new TargetIsNullOrEmptyException();
                if(target instanceof String[] targets) {
                    for (int i = 0; i < targets.length; i++) {
                        if(targets[i] == null || targets[i].isBlank())throw new TargetIsNullOrEmptyException(i);
                    }
                }
                else throw new InvalidCommandArgumentsException(null, String[].class);

            }
        }
    }
    public ServerCommand(String sender, Object target, CommandID commandID, Object argument) {
        super(sender, "SERVER");
        this.commandID = commandID;
        this.target = target;
        this.argument = argument;
        selfCheck();
    }

    public Object getArgument() {
        return argument;
    }

    public Object getTarget() {
        return target;
    }

    public CommandID getCommandID() {
        return commandID;
    }
}
