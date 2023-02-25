package general.message.servercommand;

import general.message.Message;
import general.message.servercommand.exceptions.*;

import java.io.Serial;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class ServerCommand extends Message {
    @Serial
    private static final long serialVersionUID = -6937887162749222729L;
    public enum TargetType{
        USER(String.class, false),
        USERS(String[].class, true),
        GROUP(String.class, false),
        GROUPS(String[].class, true),
        EXECUTOR(String.class,false);
        final Class<?> requiredTargetDataType;
        final boolean multiTargetsTargetType;
        TargetType(Class<?> requiredTargetDataType, boolean multiTargetsTargetType){
            this.requiredTargetDataType = requiredTargetDataType;
            this.multiTargetsTargetType = multiTargetsTargetType;
        }

        public Class<?> getRequiredTargetDataType() {
            return requiredTargetDataType;
        }

        public boolean isMultiTargetsTargetType() {
            return multiTargetsTargetType;
        }
    }
    public enum Command {
        BAN,
        UNBAN,
        ADD,
        DELETE,
        SET_RANK(Integer.class, TargetType.USER, TargetType.GROUP),
        RENAME(String.class, TargetType.USER, TargetType.GROUP),
        CHANGE_PASSWORD(String.class, TargetType.USER, TargetType.EXECUTOR);
        final Set<TargetType> avaliableTargetTypes;
        final Class<?> requiredArgumentType;
//        CHANGE_USER_RANK,
//        CHANGE_GROUP_RANK
        Command(Class<?> requiredArgumentType, TargetType ... avaliableTargetTypes){
            this.avaliableTargetTypes = new HashSet<>();
            this.avaliableTargetTypes.addAll(Arrays.stream(avaliableTargetTypes).toList());
            this.requiredArgumentType = requiredArgumentType;
        }
        Command(){
            this(null, TargetType.values());
        }
        public boolean containsTarget(TargetType targetType){
            return avaliableTargetTypes.contains(targetType);
        }
    }

    private final Object argument;
    private final Object target;
    private final Command command;
    private final TargetType targetType;
    private static HashMap<Class, Predicate<Object>> objectCheckers;
    static {
        objectCheckers = new HashMap<>();
        objectCheckers.put(String.class, obj -> {
            String str = (String) obj;
            return !(str != null && str.isBlank() && str.isEmpty());
        });
        objectCheckers.put(String[].class, obj -> {
            String []strs = (String[]) obj;
            Predicate<Object> checkString = objectCheckers.get(String.class);
            if(strs == null || strs.length == 0)return false;
            for (String str : strs) {
                if (!checkString.test(str)) return false;
            }
            return true;
        });
    }
    public void selfCheck(){
        //вначале делаем проверку основных параметров на null
        if(command == null){
            throw new CommandIsNullException();
        }
        if(targetType == null){
            throw new TargetTypeIsNullException();
        }
        //проверка является ли допустимым данный тип цели для данной команды
        if(!command.containsTarget(targetType)){
            throw new CommandDoesNotContainThisKindOfTargetType();
        }
        else{
            //если допустим, тогда проверям объект target
            if(!targetType.requiredTargetDataType.equals(target.getClass())){
                throw new TargetClassDoesNotEqualRequiredTargetDataType();
            }
            else if (!objectCheckers.get(targetType.requiredTargetDataType).test(target)){
                throw new InvalidTargetValue();
            }
        }
        //проверка на то, соответсвует ли тип аргумента для команды требуемому типу
        if(command.requiredArgumentType != null && !command.requiredArgumentType.equals(argument.getClass())){
            throw new ArgumentTypeIsNotEqualToRequired(command.requiredArgumentType, argument.getClass());
        }
        //тип соответсвует, можем проверить сам объект на корректность
        else if(!objectCheckers.get(command.requiredArgumentType).test(argument)){
                throw new InvalidArgumentValue();
            }
    }
    public ServerCommand(String sender, Object target, Command command, TargetType targetType, Object argument) {
        super(sender, "SERVER");
        this.command = command;
        if(targetType != TargetType.EXECUTOR)this.target = target;
        else this.target = sender;
        this.argument = argument;
        this.targetType = targetType;
        selfCheck();
    }

    public Object getArgument() {
        return argument;
    }

    public Object getTarget() {
        return target;
    }

    public Command getCommand() {
        return command;
    }
    public TargetType getTargetType(){
        return targetType;
    }
}
