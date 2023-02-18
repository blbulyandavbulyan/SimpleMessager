package manager.groupprocessing;

import general.privileges.Privilege;
import manager.userprocessing.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Group {
    private final String name;

    private final Set<Privilege> privileges;
    private final Set<User> users;
    private int rank;

    public Group(String groupName, Set<User> groupUsers, int groupRank, Privilege... privileges) {
        this.name = groupName;
        this.users = groupUsers != null ? groupUsers : new HashSet<>();
        this.privileges = new HashSet<>();
        this.rank = groupRank;
        this.privileges.addAll(Arrays.asList(privileges));

    }
    public String getName() {
        return name;
    }

    public Set<Privilege> getPriveleges() {
        return privileges;
    }

    public Set<User> getUsers() {
        return users;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean containsUser(User user){
        return users.contains(user);
    }
    public boolean hasPrivilege(Privilege privilege){
        return privileges.contains(privilege);
    }
    public void addPrivilege(Privilege privilege){
        privileges.add(privilege);
    }
    public void addUser(User user){
        users.add(user);
    }
}
