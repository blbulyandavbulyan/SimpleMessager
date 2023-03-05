package general.dtos;

import general.message.servercommand.ServerCommand;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class GroupDto {
    private Long id;
    private String name;
    private Set<ServerCommand.Command> allowedCommands;
    private Set<ServerCommand.InputTargetType> allowedInputTargetType;
    private Set<UserDto> users;
    private int rank;
    boolean banned = false;
}
