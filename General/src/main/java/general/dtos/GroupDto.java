package general.dtos;

import java.util.Collection;
public interface GroupDto {
    Long getId();
    String getName();
    Collection<UserDto> getUsers();
    int getRank();
    boolean getBanned();
}
