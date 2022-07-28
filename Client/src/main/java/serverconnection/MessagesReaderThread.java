package serverconnection;

import serverconnection.interfaces.MessageGetter;
import serverconnection.interfaces.MessagePrinter;

import java.io.*;
import java.net.SocketException;

public class MessagesReaderThread extends Thread{
    private MessageGetter messageGetter;
    private MessagePrinter mp;
    private boolean terminated = false;
    public MessagesReaderThread(MessageGetter messageGetter, MessagePrinter mp){
        this.messageGetter = messageGetter;
        this.mp = mp;
        start();
    }
    @Override
    public void run() {
        System.out.println("Поток для получения сообщений запущен");
        try {
            while (true){
                if(terminated)return;
                mp.printMessage(messageGetter.getMessage());
            }
        }
        catch (SocketException e){
            if(!(messageGetter.isClosed() && terminated)){
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
