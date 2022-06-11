package userprocessing;

public class User {
    private String userName;
    private String group;
    public User(String userName, String group){
        this.userName = userName;

    }

    public String getUserName() {
        return userName;
    }

    public String getGroup() {
        return group;
    }
}
