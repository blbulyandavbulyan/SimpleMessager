package manager.userprocessing;

import manager.ManagerInterface;
import manager.userprocessing.exceptions.UserAlreadyExistsException;
import manager.userprocessing.exceptions.UserDoesNotExistsException;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserManager implements Closeable, ManagerInterface<User> {
    private final Connection connection;
    private final Statement statement;
    private final PreparedStatement selectPassHashForUserFromDB;
    private final PreparedStatement selectCountUserWhereUserNameFromDB;
    private final PreparedStatement insertUserToDB;
    private final PreparedStatement deleteUserFromDB;
    private static final MessageDigest digest;
    static {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private final PreparedStatement selectUserRankFromDB;

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:server.db");
        UserManager userManager = new UserManager(conn);
        userManager.delete("test");
    }
    public UserManager(Connection connection) throws SQLException {
        if(connection == null)throw new NullPointerException("connection in class DBConnection is null");

        this.connection = connection;
        statement = connection.createStatement();
        initDB();
        selectPassHashForUserFromDB = connection.prepareStatement("SELECT PassHash FROM users WHERE UserName = ?");
        selectCountUserWhereUserNameFromDB = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE UserName = ?");
        insertUserToDB = connection.prepareStatement("INSERT INTO users(UserName, PassHash) VALUES(?, ?)");
        deleteUserFromDB = connection.prepareStatement("DELETE FROM users WHERE UserName = ?");
        selectUserRankFromDB = connection.prepareStatement("SELECT UserRank FROM users WHERE UserName = ?");
    }
    private void initDB() throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS users (UserID INTEGER, UserName TEXT NOT NULL UNIQUE, PassHash TEXT NOT NULL, UserRank INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(UserID AUTOINCREMENT));");
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
    synchronized public int getUserRank(String userName) throws SQLException {
        if(exists(userName)){
            selectUserRankFromDB.setObject(1, userName);
            ResultSet resultSet = selectUserRankFromDB.executeQuery();
            if(resultSet.next()){
                int userRank = resultSet.getInt(1);
                resultSet.close();
                return userRank;
            }
            else throw new UnknownError();
        }
        else throw new UserDoesNotExistsException();
    }
    @Override
    public void close() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(String userName, String password) {

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
    synchronized public void ban(String targetName) {

    }

    @Override
    synchronized public void unban(String targetName) {

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
}
