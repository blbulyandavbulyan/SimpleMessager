package serverconnection;

import common.MessagePrinter;

import java.io.*;
import java.net.SocketException;

public class MessagesReaderThread extends Thread{
    private ServerConnection server;
    private MessagePrinter mp;
    private boolean terminated = false;
    public MessagesReaderThread(ServerConnection server, MessagePrinter mp){
        this.server = server;
        this.mp = mp;
        start();
    }
    @Override
    public void run() {
        System.out.println("Поток для получения сообщений запущен");
        try {
            while (true){
                if(terminated)return;
                mp.printMessage(server.getMessage());
            }
        }
        catch (SocketException e){
            if(!(server.isClosed() && terminated)){
                e.printStackTrace();
            }
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public void terminate(){
        terminated = true;
    }
}
