package general.dtos;

import general.message.servercommand.ServerCommand;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private int rank;
    private GroupDto group;
    private boolean banned = false;
    private Set<ServerCommand.Command> allowedCommands;
    private Set<ServerCommand.InputTargetType> allowedInputTargetTypes;

}
