package general.message.servercommand;

import general.dtos.GroupDto;
import general.dtos.UserDto;
import general.message.Message;
import general.message.servercommand.exceptions.*;

import java.io.Serial;
import java.util.*;
import java.util.function.Predicate;

public class ServerCommand extends Message {
    @Serial
    private static final long serialVersionUID = -6937887162749222729L;
    public enum InputTargetType {
        USER(String.class, false),
        USERS(String[].class, true),
        GROUP(String.class, false),
        GROUPS(String[].class, true),
        EXECUTOR(String.class,false);
        final Class<?> requiredTargetDataType;
        final boolean multiTargetsTargetType;
        InputTargetType(Class<?> requiredTargetDataType, boolean multiTargetsTargetType){
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
    public enum OutputTargetType{
        USER(UserDto.class),
        USERS(UserDto[].class),
        GROUP(GroupDto.class),
        GROUPS(GroupDto[].class);
        final Class<?> outputType;
        OutputTargetType(Class<?> outputType){
            this.outputType = outputType;
        }
    }
    public enum Command {
        BAN,
        UNBAN,
        ADD,
        DELETE,
        GET_ENTITIES(null, null, List.of(OutputTargetType.USERS, OutputTargetType.GROUPS)),
        GET_ENTITY(null, List.of(InputTargetType.USER, InputTargetType.GROUP), List.of(OutputTargetType.GROUP, OutputTargetType.USER)),
        SET_RANK(Integer.class, List.of(InputTargetType.USER, InputTargetType.GROUP), null),
        RENAME(String.class, List.of(InputTargetType.USER, InputTargetType.GROUP), null),
        CHANGE_PASSWORD(String.class, List.of(InputTargetType.USER, InputTargetType.EXECUTOR), null);
        final Set<InputTargetType> avaliableInputTargetTypes;
        final Set<OutputTargetType> avaliableOutputTargetTypes;
        final Class<?> requiredArgumentType;
//        CHANGE_USER_RANK,
//        CHANGE_GROUP_RANK
        Command(Class<?> requiredArgumentType, List<InputTargetType> avaliableInputTargetTypes, List<OutputTargetType> avaliableOutputTargetTypes){
            this.avaliableInputTargetTypes = new HashSet<>();
            this.avaliableOutputTargetTypes = new HashSet<>();
            this.avaliableInputTargetTypes.addAll(avaliableInputTargetTypes);
            this.avaliableOutputTargetTypes.addAll(avaliableOutputTargetTypes);
            this.requiredArgumentType = requiredArgumentType;
        }
        Command(){
            this(null, List.of(InputTargetType.values()), null);
        }
        public boolean containsTarget(InputTargetType inputTargetType){
            return avaliableInputTargetTypes.contains(inputTargetType);
        }
    }

    private final Object argument;
    private final Object target;
    private final Command command;
    private final InputTargetType inputTargetType;
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
        if(inputTargetType == null){
            throw new TargetTypeIsNullException();
        }
        //проверка является ли допустимым данный тип цели для данной команды
        if(!command.containsTarget(inputTargetType)){
            throw new CommandDoesNotContainThisKindOfTargetType();
        }
        else{
            //если допустим, тогда проверям объект target
            if(!inputTargetType.requiredTargetDataType.equals(target.getClass())){
                throw new TargetClassDoesNotEqualRequiredTargetDataType();
            }
            else if (!objectCheckers.get(inputTargetType.requiredTargetDataType).test(target)){
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
    public ServerCommand(String sender, Object target, Command command, InputTargetType inputTargetType, Object argument) {
        super(sender, "SERVER");
        this.command = command;
        if(inputTargetType != InputTargetType.EXECUTOR)this.target = target;
        else this.target = sender;
        this.argument = argument;
        this.inputTargetType = inputTargetType;
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
    public InputTargetType getTargetType(){
        return inputTargetType;
    }
}
