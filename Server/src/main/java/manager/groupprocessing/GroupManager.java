package manager.groupprocessing;

import general.entities.Group;
import manager.ManagerInterface;

import java.sql.Connection;

public class GroupManager implements ManagerInterface<Group> {
    //fixme this class is empty!
    public GroupManager(Connection connection){

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
    public void setRank(String targetName, Integer rank) {

    }

    @Override
    public boolean banned(String targetName) {
        return false;
    }
}
