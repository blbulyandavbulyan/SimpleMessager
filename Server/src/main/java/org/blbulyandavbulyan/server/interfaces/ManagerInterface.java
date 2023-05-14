package org.blbulyandavbulyan.server.interfaces;
//этот интерфейс предназначен для обобщения классов UserManager и GroupManager
public interface ManagerInterface<T> {
    int getRank(String targetName);
    void setRank(String targetName, Integer rank);
    void rename(String targetName, String newName);
    void delete(String targetName);
    void add(T obj);
    T get(String userName);
    T[] getAll();
    void ban(String targetName);
    void unban(String targetName);
    boolean exists(String targetName);
    boolean banned(String targetName);

}
