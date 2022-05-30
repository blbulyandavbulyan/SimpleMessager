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
                case "DISCONNECT ALL" -> Server.clearClients();

            }
        }
    }
    public void terminate(){
        termintaed = true;
    }
}
