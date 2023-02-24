package manager.groupprocessing;

import manager.ManagerInterface;

public class GroupManager implements ManagerInterface<Group> {
    //fixme this class is empty!
    public GroupManager(){

    }

    @Override
    public void rename(String targetName, String newName) {

    }

    @Override
    public void delete(String target) {

    }

    @Override
    public void add(Group obj) {

    }

    @Override
    public void ban(String target) {

    }

    @Override
    public void unban(String target) {

    }

    @Override
    public boolean exists(String targetName) {
        return false;
    }

    @Override
    public int getRank(String groupName) {
        return 0;
    }

    @Override
    public boolean banned(String targetName) {
        return false;
    }
}
