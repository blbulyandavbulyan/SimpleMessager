package threads;

import common.Server;
import java.io.InputStream;
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
                case "!DISCONNECT ALL" -> Server.clearClients();
                case "!SHOW CHAT", "!DISABLE CHAT SHOWING" -> Server.setShowMessagesFromUser(false);
                case "!DON'T SHOW CHAT", "!ENABLE CHAT SHOWING"->Server.setShowMessagesFromUser(true);
                case "!LIST CLIENTS"->{
                    Server.setShowMessagesFromUser(false);
                    for (ClientServerThread client : Server.getClients()) {

                    }
                }
            }
        }
    }
    public void terminate(){
        termintaed = true;
    }
}
