package threads;

import common.Server;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Scanner;
public class ServerCommandReaderThread extends Thread{
    private Scanner commandInput;
    private boolean termintaed = false;
    public ServerCommandReaderThread(InputStream commandInput){
        this.commandInput = new Scanner(commandInput);
    }
    @Override
    public void run() {
        while(!termintaed){
            String command = commandInput.nextLine();
            switch(command){
                case "!DISCONNECT ALL" -> {
                    Server.clearClients();
                    Server.clearUnregisteredClients();
                    Server.serverPrint("SUCCESSFUL!");
                }
                case "!DISCONNECT AUTHORIZED CLIENTS"->{
                    Server.clearClients();
                    Server.serverPrint("SUCCESSFUL!");
                }
                case "!DISCONNECT UNAUTHORIZED CLIENTS"->{
                    Server.clearUnregisteredClients();
                    Server.serverPrint("SUCCESSFUL!");
                }
                case  "!DON'T SHOW CHAT", "!DISABLE CHAT SHOWING" -> Server.setShowMessagesFromUser(false);
                case "!SHOW CHAT", "!ENABLE CHAT SHOWING"->Server.setShowMessagesFromUser(true);
                case "!LIST AUTHORIZED CLIENTS"->{
                    //Server.setShowMessagesFromUser(false);
                    Collection<ClientServerThread> clients = Server.getClients();
                    Server.serverPrint("Подключено авторизованных клиентов в количестве: %d\n".formatted(clients.size()));
                    for (ClientServerThread client : clients) {

                    }

                }
                case "!COUNT AUTHORIZED CLIENTS"->{
                    System.out.printf("Подключено авторизованных клиентов в количестве: %d\n", Server.getClients().size());
                }
                case "!SHOW USERS" ->{

                }
            }
        }
    }
    public void terminate(){
        termintaed = true;
    }
}
