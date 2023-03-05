package threads;

import common.Server;
import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;
public class ServerCommandReaderThread extends Thread{
    private final Scanner commandInput;
    private boolean terminated = false;
    public ServerCommandReaderThread(InputStream commandInput){
        this.commandInput = new Scanner(commandInput);
    }
    @Override
    public void run() {
//        while(!terminated){
//            String command = commandInput.nextLine();
//            switch(command){
//                case "!DISCONNECT ALL" -> {
//                    server.clearClients();
//                    server.clearUnregisteredClients();
//                    server.printInfo("SUCCESSFUL!");
//                }
//                case "!DISCONNECT AUTHORIZED CLIENTS"->{
//                    server.clearClients();
//                    server.printInfo("SUCCESSFUL!");
//                }
//                case "!DISCONNECT UNAUTHORIZED CLIENTS"->{
//                    server.clearUnregisteredClients();
//                    server.printInfo("SUCCESSFUL!");
//                }
//                case  "!DON'T SHOW CHAT", "!DISABLE CHAT SHOWING" -> server.setShowMessagesFromUser(false);
//                case "!SHOW CHAT", "!ENABLE CHAT SHOWING"->server.setShowMessagesFromUser(true);
//                case "!LIST AUTHORIZED CLIENTS"->{
//                    //server.setShowMessagesFromUser(false);
//                    Collection<ClientProcessingServerThread> clients = server.getClients();
//                    server.printInfo("Подключено авторизованных клиентов в количестве: %d\n".formatted(clients.size()));
//                    for (ClientProcessingServerThread client : clients) {
//
//                    }
//
//                }
//                case "!COUNT AUTHORIZED CLIENTS"->{
//                    System.out.printf("Подключено авторизованных клиентов в количестве: %d\n", server.getClients().size());
//                }
//                case "!SHOW USERS" ->{
//
//                }
//            }
//        }
    }
    public void terminate(){
        terminated = true;
    }
}
