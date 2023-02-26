package common;

import common.exceptions.ServerException;
import manager.groupprocessing.GroupManager;
import threads.ClientProcessingServerThread;
import threads.LoginOrRegisterClientThread;
import threads.exceptions.ServerThreadException;
import manager.userprocessing.UserManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Server extends Thread{
//    private static int PORT = 1234;
//    private static String listenAddress = "localhost";
//    private static int backlog = 0;
    private final Map<String, ClientProcessingServerThread> clients = new HashMap<>();
    private final Set<LoginOrRegisterClientThread> unregisteredUsers = new HashSet<>();
    private static final Map<String, String> helpForArguments = new HashMap<>();
    private final PrintStream sPs;
    private boolean showMessagesFromUser = true;
    private StartupParameters startupParameters;
    private Connection connection;
    private UserManager userManager;
    private GroupManager groupManager;
    private void initDb() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS groups(GroupID INTEGER, GroupName TEXT NOT NULL UNIQUE, GroupRank INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(GroupID AUTOINCREMENT));");
        statement.execute("CREATE TABLE IF NOT EXISTS users (UserID INTEGER, UserName TEXT NOT NULL UNIQUE, PassHash TEXT NOT NULL, Banned INTEGER DEFAULT 0, UserRank INTEGER NOT NULL DEFAULT 0, gid INTEGER, CONSTRAINT fk_1 FOREIGN KEY (gid) REFERENCES groups(GroupID), PRIMARY KEY(UserID AUTOINCREMENT));");
        statement.execute("CREATE TABLE IF NOT EXISTS allowed_commands_for_groups(id INTEGER, AllowedCommand TEXT NOT NULL, gid INTEGER NOT NULL, UNIQUE(AllowedCommand, gid), CONSTRAINT fk_2 FOREIGN KEY(gid) REFERENCES groups(GroupID), PRIMARY KEY(id AUTOINCREMENT));");
        statement.execute("CREATE TABLE IF NOT EXISTS allowed_commands_for_users(id INTEGER, AllowedCommand TEXT NOT NULL, uid INTEGER NOT NULL, UNIQUE(AllowedCommand, uid), CONSTRAINT fk_3 FOREIGN KEY(uid) REFERENCES users(UserID), PRIMARY KEY(id AUTOINCREMENT));");
        statement.close();
    }
    public Server(StartupParameters startupParameters, Connection connection, PrintStream printStream) throws SQLException {
        this.sPs = printStream;
        this.startupParameters = startupParameters;
        this.connection = connection;
        initDb();
        this.userManager = new UserManager(connection);
        this.groupManager = new GroupManager(connection);
    }

    @Override
    public void run() {
        try(ServerSocket sSocket = new ServerSocket()){
            InetSocketAddress bindAddr = new InetSocketAddress(startupParameters.listenAddress, startupParameters.port);
            sSocket.bind(bindAddr, startupParameters.backlog);
            while(true){
                System.out.println("Сервер запущен, ожидаю подключения....");
                Socket client = null;
                try{
                    client = sSocket.accept();
                    System.out.println("Новое соединение установлено!");
                    unregisteredUsers.add(new LoginOrRegisterClientThread(client, this));
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

    private static class StartupParameters{
        int port = 1234;
        String listenAddress = "localhost";
        String dbSubname = "server.db";
        String dbmsName = "sqlite";
        int backlog = 0;
    }
    private static class RunErrorCodes {
        private static int nextErrorCode = -1;
        enum RUN_ERROR_CODES {
            PORT_IS_NOT_A_NUMBER(),
            PORT_OUT_OF_RANGE(),
            INVALID_ARGUMENT_COUNTS(),
            INVALID_ARGUMENT(),
            BACKLOG_IS_NOT_A_NUMBER(),
            NO_HELP_FOR_ARGUMENT();
            public final int errorCode;

            RUN_ERROR_CODES() {
                this.errorCode = nextErrorCode--;
            }
        }
    }
    static {
        helpForArguments.put(
                "--listen-port",
                "Использование: --listen-port %port%, где %port% - число в диапазоне от 0 до 65535,\n это ни что иное как порт, на котором сервер будет слушать соединения."
        );
        helpForArguments.put(
                "--listen-address",
                "Использование: --listen-address %address%, где %address% - ip адрес, на котором будет принимать соединения сервер."
        );
        helpForArguments.put(
                "--backlog",
                "Использование: --backlog %backlog%, где %backlog% - число, которое означает максимальное число клиентов в очереди на подключение,\n если указать значение <= 0, то будет выбрано значение по умолчанию."
        );
        helpForArguments.put(
                "--db-subname",
                "Использование: --db-subname %subname%, где %subname% - параметр подключения к БД.\n В случае если драйвер базы стоит по умолчанию, то данный параметр просто представляет путь к файлу базы данных SQLite.\n Он не обязательно должен существовать, если он не существует, то он будет создан.\n Если же выбран другой драйвер, то значение параметра, зависит от него. \nВсе необходимые таблицы добавляются автоматически, если в БД их нет"
        );
        helpForArguments.put(
                "--dbms-name",
                "Использование: --dbms-name %driver-name%, где %driver-name% - имя драйвера, для подключения к базе.\n Драйвер должен быть включён в jar архив, по умолчанию поддерживается только SQLite"
        );
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
    synchronized public void print(Object obj){
        sPs.println(obj);
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
    public static void main(String[] args) throws SQLException {
        StartupParameters startupParameters = parseCommandLineArguments(args);
        Server server = new Server(startupParameters, java.sql.DriverManager.getConnection(String.format("jdbc:%s:%s", startupParameters.dbmsName, startupParameters.dbSubname)), System.out);
        server.start();
    }
    private static StartupParameters parseCommandLineArguments(String[] args){
        StartupParameters startupParameters = new StartupParameters();
        try{
            for(int i = 0; i < args.length;){
                switch (args[i]){
                    case "help", "-help", "--help"->{
                        try{
                            String argumentForHelp = args[i + 1];
                            String helpForCommand = helpForArguments.get(argumentForHelp);
                            if(helpForCommand == null){
                                System.err.printf("Справки для аргумента %s нет\n", argumentForHelp);
                                System.exit(RunErrorCodes.RUN_ERROR_CODES.NO_HELP_FOR_ARGUMENT.errorCode);
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            System.out.println("Для справки по определённому аргументу введите: help argument,\n где argument - интересующий вас argument");
                            for (String argHelp : helpForArguments.values()) {
                                System.out.println(argHelp);
                            }
                        }
                        System.exit(0);
                    }
                    case "--listen-port" ->{
                        try{
                            int port = Integer.parseInt(args[i + 1]);
                            if(port < 0 || port > 65535){
                                System.err.println("Введённый номер порта после --listen-port не принадлежит диапазону [0; 665535]");
                                System.exit(RunErrorCodes.RUN_ERROR_CODES.PORT_OUT_OF_RANGE.errorCode);
                            }
                            else startupParameters.port = port;
                            i+=2;
                        }
                        catch (NumberFormatException e){
                            System.err.println("Вы ввели не число после --listen-port");
                            e.printStackTrace();
                            System.exit(RunErrorCodes.RUN_ERROR_CODES.PORT_IS_NOT_A_NUMBER.errorCode);
                        }
                    }
                    case "--listen-address" ->{
                        startupParameters.listenAddress = args[i+1];
                        i+=2;
                    }
                    case "--backlog" ->{
                        try{
                            startupParameters.backlog = Integer.parseInt(args[i+1]);
                            i+=2;
                        }
                        catch (NumberFormatException e){
                            System.exit(RunErrorCodes.RUN_ERROR_CODES.BACKLOG_IS_NOT_A_NUMBER.errorCode);
                        }
                    }
                    case "--db-subname"->{
                        startupParameters.dbSubname = args[i+1];
                        i+=2;
                    }
                    case "--dbms-name"->{
                        startupParameters.dbmsName = args[i+1];
                        i+=2;
                    }
                    default -> {
                        System.err.printf("Неверный аргумент %s", args[i]);
                        System.exit(RunErrorCodes.RUN_ERROR_CODES.INVALID_ARGUMENT.errorCode);
                    }
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.err.println("Кажется вы указали недостаточно аргументов. ");
            System.exit(RunErrorCodes.RUN_ERROR_CODES.INVALID_ARGUMENT_COUNTS.errorCode);
        }
        return startupParameters;
    }

    public UserManager getUserManager() {
        return userManager;
    }
    public  GroupManager getGroupManager(){
        return groupManager;
    }
}
