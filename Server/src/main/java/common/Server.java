package common;

import general.message.Message;
import threads.ClientServerThread;
import threads.LoginOrRegisterClientThread;
import userprocessing.UserManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    static final int PORT = 1234;
    static final Map<String, ClientServerThread> clients = new HashMap<>();
    static final Set<LoginOrRegisterClientThread> unregisteredUsers = new HashSet<>();
    static final PrintStream sPs = System.out;
    static final UserManager userManager;
    static final  java.sql.Connection dbConnection;
    static boolean showMessagesFromUser = true;
    static {
        try{
            Class.forName("org.sqlite.JDBC");
            dbConnection = java.sql.DriverManager.getConnection("jdbc:sqlite:server.db");
            userManager = new UserManager(dbConnection);
        }
        catch (java.sql.SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeClient(String clientName){
        clients.remove(clientName);
    }

    public static ClientServerThread addClient(ClientServerThread clientThread){
        return clients.put(clientThread.getClientName(), clientThread);
    }

    public static boolean IsClientExists(String clientName){
        return clients.containsKey(clientName);
    }

    public static Collection<ClientServerThread> getClients(){
        return clients.values();
    }

    public static ClientServerThread getClient(String clientName){
        return clients.get(clientName);
    }

    public static Collection<LoginOrRegisterClientThread> getUnregisteredClients(){
        return unregisteredUsers;
    }

    public static void clearClients(){
        for (ClientServerThread client : Server.getClients()) {
            client.terminate();
        }
        Server.clients.clear();
    }

    public static void clearUnregisteredClients(){
        for(LoginOrRegisterClientThread uclient : unregisteredUsers){
            uclient.terminate();
        }
        Server.unregisteredUsers.clear();
    }
    synchronized public void serverPrint(Object obj){
        sPs.println(obj);
    }
    public static void printMessage(Message msg){
        if(showMessagesFromUser)sPs.println(msg);
    }
    public static void setShowMessagesFromUser(boolean showMessagesFromUser){
        Server.showMessagesFromUser = showMessagesFromUser;
    }
    public static PrintStream getsPs() {
        return sPs;
    }
    public static void removeUnregisteredClient(LoginOrRegisterClientThread lorClientThread){
        unregisteredUsers.remove(lorClientThread);
    }
    public static void main(String[] args) throws IOException {
        try(ServerSocket sSocket = new ServerSocket(PORT)){
            while(true){
                System.out.println("Сервер запущен, ожидаю подключения....");
                Socket client = sSocket.accept();
                System.out.println("Новое соединение установлено!");
                unregisteredUsers.add(new LoginOrRegisterClientThread(client, userManager));
            }
        }
        finally {
            for (var client : clients.values())client.terminate();
            for(var unnamedClient : unregisteredUsers)unnamedClient.terminate();
        }
    }

}
