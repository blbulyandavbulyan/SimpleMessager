package general.dtos;

import general.message.servercommand.ServerCommand;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
public interface UserDto {
    Long getId();
    String getName();
    int getRank();
    GroupDto getGroup();
    boolean getBanned();
//    Set<ServerCommand.Command> getAllowedCommands();
//    Set<ServerCommand.InputTargetType> getAllowedInputTargetTypes();

}
