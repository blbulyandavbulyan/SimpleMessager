package general.dtos;

import general.message.servercommand.ServerCommand;

import java.util.Collection;
import java.util.Set;
public interface GroupDto {
    Long getId();
    String getName();
    Collection<UserDto> getUsers();
    int getRank();
    boolean getBanned();
}
