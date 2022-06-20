package userprocessing;

public class User {
    private final String userName;
    private final String group;
    public User(String userName, String group){
        this.userName = userName;
        this.group = group;
    }

    public String getUserName() {
        return userName;
    }

    public String getGroup() {
        return group;
    }
}
