package org.blbulyandavbulyan.smserver.common;

import org.blbulyandavbulyan.smserver.common.exceptions.ServerException;
import org.blbulyandavbulyan.smserver.interfaces.ServerLogger;
import org.blbulyandavbulyan.smserver.spring.beans.services.group.GroupService;
import org.blbulyandavbulyan.smserver.spring.beans.services.user.UserService;
import org.slf4j.Logger;
import org.blbulyandavbulyan.smserver.interfaces.loginandregister.LoginAndRegisterUserInterface;
import org.slf4j.LoggerFactory;
import org.blbulyandavbulyan.smserver.threads.ClientProcessingServerThread;
import org.blbulyandavbulyan.smserver.threads.LoginOrRegisterClientThread;
import org.blbulyandavbulyan.smserver.threads.exceptions.ServerThreadException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
public class Server extends Thread implements ServerLogger {
    private final Map<String, ClientProcessingServerThread> clients = new HashMap<>();
    private final Set<LoginOrRegisterClientThread> unregisteredUsers = new HashSet<>();
    private final Logger logger = LoggerFactory.getLogger(Server.class);

    private final StartupParameters startupParameters;
    private final UserService userService;
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
                    logger.error("Ошибка на серрвере при работе с клиентом", e);
                    try{
                        if(client != null)client.close();
                    }
                    catch (IOException ignore){

                    }
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

    @Override
    public void error(String s, Exception e) {
        logger.error(s, e);
    }
}
