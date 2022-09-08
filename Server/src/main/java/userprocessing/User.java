package userprocessing;

import groupprocessing.Group;

import java.util.HashSet;
import java.util.Set;

public class User {
    private final String userName;
    private int rank;
    private Group group;
    private final Set<Privilege> privileges;
    public User(String userName, Group group, int userRank){
        this.userName = userName;
        this.group = group;
        this.rank = userRank;
        privileges = new HashSet<>();
    }

    public String getUserName() {
        return userName;
    }

    public Group getGroup() {
        return group;
    }
    public int getRank(){
        return rank + (group != null ? group.getRank() : 0);
    }
    public boolean hasPrivelege(Privilege privilege){
        return privileges.contains(privilege) || (group == null || group.hasPrivilege(privilege));
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    public void addPrivilege(Privilege privilege){
        privileges.add(privilege);
    }
}
