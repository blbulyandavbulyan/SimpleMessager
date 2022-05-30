package common;

import threads.ClientServerThread;
import threads.UnnamedClientThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    static final int PORT = 1234;
    static final Map<String, ClientServerThread> clients = new HashMap<>();
    static final Set<UnnamedClientThread> unnamedClients = new HashSet<>();
    static final PrintStream sPs = System.out;
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
    public static void clearClients(){
        for (ClientServerThread client : Server.getClients()) {
            client.terminate();
        }
        Server.clients.clear();
    }
    public static PrintStream getsPs() {
        return sPs;
    }
    public static void removeUnnamedClient(UnnamedClientThread uct){
        unnamedClients.remove(uct);
    }
    public static void main(String[] args) throws IOException {
        try(ServerSocket sSocket = new ServerSocket(PORT)){
            while(true){
                System.out.println("Сервер запущен, ожидаю подключения....");
                Socket client = sSocket.accept();
                System.out.println("Новое соединение установлено!");
                InputStream clientInput = client.getInputStream();
                String userName;
                {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientInput));
                    userName = in.readLine();
                    if(clients.containsKey(userName)){
                        unnamedClients.add(new UnnamedClientThread(client));
                        continue;
                    }
                }
                System.out.printf("Пользователь %s зарегистрирован.\n", userName);
                addClient(new ClientServerThread(client, userName));
            }
        }
        finally {
            for (var client : clients.values())client.terminate();
        }
    }

}
