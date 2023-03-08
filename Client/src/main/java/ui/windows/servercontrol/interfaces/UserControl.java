package ui.windows.servercontrol.interfaces;

import general.dtos.UserDto;
import ui.windows.servercontrol.interfaces.exceptions.EntityNotFoundException;
import ui.windows.servercontrol.interfaces.exceptions.PermissionsDeniedException;

public interface UserControl extends GeneralControlInterface<UserDto>{
    void changePassword(String username, String newPassword) throws EntityNotFoundException, PermissionsDeniedException;
}
