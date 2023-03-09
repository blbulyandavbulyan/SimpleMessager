package ui.windows.servercontrol.interfaces;

import general.dtos.GroupDto;
import general.message.servercommand.ServerCommand;
import ui.windows.servercontrol.interfaces.exceptions.EntityNotFoundException;
import ui.windows.servercontrol.interfaces.exceptions.PermissionsDeniedException;

public interface GeneralControlInterface<T> {
    //данный интерфейс предназначен для того чтобы от него унаследовались интерфейсы GroupControl, UserControl
    //в нём расположены операции, которые доступны как для групп, так и для пользователей
    boolean isBanned(String name)throws EntityNotFoundException, PermissionsDeniedException;
    void ban(String name) throws EntityNotFoundException, PermissionsDeniedException;
    void unban(String name) throws EntityNotFoundException, PermissionsDeniedException;
    void delete(String name) throws EntityNotFoundException, PermissionsDeniedException;
    void add(GroupDto groupDto) throws EntityNotFoundException, PermissionsDeniedException;
    void updateExists(GroupDto groupDto);
    T[] findAll() throws PermissionsDeniedException;
    String[] findAllTargetNames() throws PermissionsDeniedException;
    T findByName(String name) throws EntityNotFoundException, PermissionsDeniedException;
    boolean can(String executorName, String targetName, ServerCommand.Command command) throws EntityNotFoundException;
}
