package manager.userprocessing;

import manager.ManagerInterface;
import entities.User;
import manager.userprocessing.exceptions.UserAlreadyExistsException;
import manager.userprocessing.exceptions.UserDoesNotExistsException;
import manager.userprocessing.exceptions.UserManagerException;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserManager implements Closeable, ManagerInterface<User> {
    private final PreparedStatement selectPassHashForUserFromDB;
    private final PreparedStatement selectCountUserWhereUserNameFromDB;
    private final PreparedStatement insertUserToDB;
    private final PreparedStatement deleteUserFromDB;
    private final PreparedStatement updateBannedForUser;
    private final PreparedStatement selectBannedForUser;
    private final PreparedStatement updateUserPassword;
    private static final MessageDigest digest;
    static {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private final PreparedStatement selectUserRankFromDB;

//    public static void main(String[] args) throws SQLException {
//        Connection conn = DriverManager.getConnection("jdbc:sqlite:server.db");
//        UserManager userManager = new UserManager(conn);
//        userManager.delete("test");
//    }
    public UserManager(Connection connection) throws SQLException {
        if(connection == null)throw new NullPointerException("connection in class DBConnection is null");
        selectPassHashForUserFromDB = connection.prepareStatement("SELECT PassHash FROM users WHERE UserName = ?");
        selectCountUserWhereUserNameFromDB = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE UserName = ?");
        insertUserToDB = connection.prepareStatement("INSERT INTO users(UserName, PassHash) VALUES(?, ?)");
        deleteUserFromDB = connection.prepareStatement("DELETE FROM users WHERE UserName = ?");
        selectUserRankFromDB = connection.prepareStatement("SELECT UserRank FROM users WHERE UserName = ?");
        updateBannedForUser = connection.prepareStatement("UPDATE users SET Banned = ? WHERE UserName = ?");
        selectBannedForUser = connection.prepareStatement("SELECT Banned FROM users WHERE UserName = ?");
        updateUserPassword = connection.prepareStatement("UPDATE users SET PassHash = ? WHERE UserName = ?");
    }
    static private String getPasswordHash(String password){
        byte []hash;
        synchronized (digest){
             hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        }
        byte a = -1;
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public boolean login(String userName, String password) throws SQLException {
        selectPassHashForUserFromDB.setObject(1, userName);
        var resultSet = selectPassHashForUserFromDB.executeQuery();
        if(resultSet.next()){
            String passHashFromDB = resultSet.getString(1);
            return passHashFromDB.equals(getPasswordHash(password));
        }
        else return false;
    }
     synchronized public void registerUser(String userName, String password) throws SQLException, UserAlreadyExistsException {
        if(!exists(userName)){
//            add(new User(userName, ));
            insertUserToDB.setObject(1, userName);
            insertUserToDB.setObject(2, getPasswordHash(password));
            insertUserToDB.execute();
            //return insertUserToDB.getUpdateCount();
        }
        else throw new UserAlreadyExistsException();
    }
    @Override
    synchronized public int getRank(String userName){
        try{
            if (exists(userName)) {
                selectUserRankFromDB.setObject(1, userName);
                ResultSet resultSet = selectUserRankFromDB.executeQuery();
                if (resultSet.next()) {
                    int userRank = resultSet.getInt(1);
                    resultSet.close();
                    return userRank;
                } else throw new UnknownError();
            } else throw new UserDoesNotExistsException();
        }
        catch (SQLException e){
            throw new UserManagerException(e);
        }
    }

    @Override
    public void setRank(String targetName, Integer rank) {

    }

    @Override
    public void close() {
        try {
            selectPassHashForUserFromDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            selectCountUserWhereUserNameFromDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            insertUserToDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            deleteUserFromDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(String userName, String password){
        try{
            if(exists(userName)){
                updateUserPassword.setObject(1, password);
                updateUserPassword.setObject(2, userName);
                updateUserPassword.executeUpdate();
            }
            else throw new UserDoesNotExistsException();
        }
        catch (SQLException e){
            throw new UserManagerException(e);
        }
    }

    @Override
    public void rename(String targetName, String newName) {

    }

    @Override
    synchronized public void delete(String userName) {
        try{
            deleteUserFromDB.setObject(1, userName);
            deleteUserFromDB.execute();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    synchronized public void add(User obj) {

    }
    @Override
    synchronized public void ban(String userName) {
        try{
            if(exists(userName)){
                updateBannedForUser.setObject(1, 1);
                updateBannedForUser.setObject(2, userName);
                updateBannedForUser.executeUpdate();
            }
            else throw new UserDoesNotExistsException();
        }
        catch (SQLException e){
            throw new UserManagerException(e);
        }
    }
    @Override
    synchronized public void unban(String userName) {
        try{
            if (exists(userName)) {
                updateBannedForUser.setObject(1, 0);
                updateBannedForUser.setObject(2, userName);
                updateBannedForUser.executeUpdate();
            } else throw new UserDoesNotExistsException();
        }
        catch (SQLException e){
            throw new UserManagerException(e);
        }
    }
    synchronized public void unban(String[] userNames) throws SQLException {
        for (String userName : userNames)
            unban(userName);
    }

    @Override
    public boolean exists(String userName) {
        try{
            selectCountUserWhereUserNameFromDB.setObject(1, userName);
            var resultSet = selectCountUserWhereUserNameFromDB.executeQuery();
            return resultSet.getLong(1) > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean banned(String userName) {
        try{
            selectBannedForUser.setObject(1, userName);
            int banned = selectBannedForUser.executeQuery().getInt(1);
            return banned == 1;
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
