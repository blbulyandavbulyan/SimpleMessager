package common;

import general.message.textmessage.TextMessage;
import threads.ClientServerThread;
import threads.LoginOrRegisterClientThread;
import userprocessing.UserManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;

public class Server {
    private static int PORT = 1234;
    private static String listenAddress = "localhost";
    private static int backlog = 0;
    static final Map<String, ClientServerThread> clients = new HashMap<>();
    static final Set<LoginOrRegisterClientThread> unregisteredUsers = new HashSet<>();
    static final Map<String, String> helpForArguments = new HashMap<>();
    static final PrintStream sPs = System.out;
    static UserManager userManager;
    static java.sql.Connection dbConnection;
    static boolean showMessagesFromUser = true;
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
                "Использование: --listen-address %address%, где %address% - ip адресс, на котором будет принимать соединения сервер."
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

    public static void removeClient(String clientName){
        clients.remove(clientName);
    }

    public static void addClient(ClientServerThread clientThread){
        clients.put(clientThread.getClientName(), clientThread);
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
    public static void printMessage(TextMessage msg){
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
        String dbSubname = "server.db";
        String dbmsName = "sqlite";
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
                            PORT = Integer.parseInt(args[i + 1]);
                            if(PORT < 0 || PORT > 65535){
                                System.err.println("Введённый номер порта после --listen-port не принадлежит диапазону [0; 665535]");
                                System.exit(RunErrorCodes.RUN_ERROR_CODES.PORT_OUT_OF_RANGE.errorCode);
                            }
                            i+=2;
                        }
                        catch (NumberFormatException e){
                            System.err.println("Вы ввели не число после --listen-port");
                            e.printStackTrace();
                            System.exit(RunErrorCodes.RUN_ERROR_CODES.PORT_IS_NOT_A_NUMBER.errorCode);
                        }
                    }
                    case "--listen-address" ->{
                        listenAddress = args[i+1];
                        i+=2;
                    }
                    case "--backlog" ->{
                        try{
                            backlog = Integer.parseInt(args[i+1]);
                            i+=2;
                        }
                        catch (NumberFormatException e){
                            System.exit(RunErrorCodes.RUN_ERROR_CODES.BACKLOG_IS_NOT_A_NUMBER.errorCode);
                        }
                    }
                    case "--db-subname"->{
                        dbSubname = args[i+1];
                        i+=2;
                    }
                    case "--dbms-name"->{
                        dbmsName = args[i+1];
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
            return;
        }
        try(ServerSocket sSocket = new ServerSocket(); UserManager userManager = new UserManager(java.sql.DriverManager.getConnection(String.format("jdbc:%s:%s", dbmsName, dbSubname)))){
            InetSocketAddress bindAddr = new InetSocketAddress(listenAddress, PORT);
            sSocket.bind(bindAddr, backlog);
            while(true){
                System.out.println("Сервер запущен, ожидаю подключения....");
                Socket client = sSocket.accept();
                System.out.println("Новое соединение установлено!");
                unregisteredUsers.add(new LoginOrRegisterClientThread(client, userManager));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            for (var client : clients.values())client.terminate();
            for(var unnamedClient : unregisteredUsers)unnamedClient.terminate();
        }
    }

}
