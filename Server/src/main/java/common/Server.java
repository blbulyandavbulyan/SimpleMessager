package common;

import common.exceptions.ServerException;
import interfaces.ServerLogger;
import org.slf4j.Logger;
import interfaces.loginandregister.LoginAndRegisterUserInterface;
import org.slf4j.LoggerFactory;
import spring.beans.services.group.GroupService;
import spring.beans.services.user.UserService;
import threads.ClientProcessingServerThread;
import threads.LoginOrRegisterClientThread;
import threads.exceptions.ServerThreadException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
public class Server extends Thread implements ServerLogger {
    private final Map<String, ClientProcessingServerThread> clients = new HashMap<>();
    private final Set<LoginOrRegisterClientThread> unregisteredUsers = new HashSet<>();
    private final Logger logger = LoggerFactory.getLogger(Server.class);

    private boolean showMessagesFromUser = true;
    private StartupParameters startupParameters;
    private UserService userService;
    private GroupService groupService;
    public Server(StartupParameters startupParameters, UserService userService, GroupService groupService){
        this.userService = userService;
        this.groupService = groupService;
        this.startupParameters = startupParameters;
    }

    @Override
    public void run() {
        try(ServerSocket sSocket = new ServerSocket()){
            InetSocketAddress bindAddr = new InetSocketAddress(startupParameters.listenAddress, startupParameters.port);
            sSocket.bind(bindAddr, startupParameters.backlog);
            while(true){
                logger.info("Сервер запущен, ожидаю подключения....");
                Socket client = null;
                try{
                    client = sSocket.accept();
                    logger.info("Новое соединение установлено!");
                    unregisteredUsers.add(new LoginOrRegisterClientThread(client, this, this));
                }
                catch (ServerThreadException e){
                    e.printStackTrace();
                    try{
                        if(client != null)client.close();
                    }
                    catch (IOException ignore){

                    }
                }
                catch (Throwable throwable){
                    throwable.printStackTrace();
                }
            }
        } catch (IOException e ) {
            throw new ServerException(e);
        } finally {
            for (var client : clients.values())client.terminate();
            for(var unnamedClient : unregisteredUsers)unnamedClient.terminate();
        }
    }

    synchronized public void addClient(ClientProcessingServerThread clientThread){
        clients.put(clientThread.getClientName(), clientThread);
    }

    public boolean IsClientExists(String clientName){
        return clients.containsKey(clientName);
    }

    public Collection<ClientProcessingServerThread> getClients(){
        return clients.values();
    }

    public ClientProcessingServerThread getClient(String clientName){
        return clients.get(clientName);
    }

    public Collection<LoginOrRegisterClientThread> getUnregisteredClients(){
        return unregisteredUsers;
    }

    public void clearClients(){
        for (ClientProcessingServerThread client : getClients()) {
            client.terminate();
        }
        clients.clear();
    }

    public void clearUnregisteredClients(){
        for(LoginOrRegisterClientThread uclient : unregisteredUsers){
            uclient.terminate();
        }
        unregisteredUsers.clear();
    }
    public void setShowMessagesFromUser(boolean showMessagesFromUser){
        this.showMessagesFromUser = showMessagesFromUser;
    }
    public void removeUnregisteredClient(LoginOrRegisterClientThread lorClientThread){
        unregisteredUsers.remove(lorClientThread);
    }
    public void removeClient(String clientName){
        clients.remove(clientName);
    }

    public LoginAndRegisterUserInterface getLoginOrRegisterUser(){
        return userService;
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }
    @Override
    public void info(String msg) {
        logger.info(msg);
    }
}
