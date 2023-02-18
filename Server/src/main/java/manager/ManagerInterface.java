package manager;
//этот интерфейс предназначен для обобщения классов UserManager и GroupManager
public interface ManagerInterface<T> {
    void delete(String targetName);
    void add(T obj);
    void ban(String targetName);
    void unban(String targetName);
    boolean exists(String targetName);
}
