package org.blbulyandavbulyan.smclient.serverconnection;

import org.blbulyandavbulyan.smgeneral.message.Message;
import org.blbulyandavbulyan.smclient.serverconnection.interfaces.MessageGetter;

import java.io.*;
import java.net.SocketException;
import java.util.function.Consumer;

public class MessagesReaderThread extends Thread{
    private final MessageGetter messageGetter;
    private final Consumer<Message> messageConsumer;
    private boolean terminated = false;
    public MessagesReaderThread(MessageGetter messageGetter, Consumer<Message> messageConsumer){
        this.messageGetter = messageGetter;
        this.messageConsumer = messageConsumer;
        start();
    }
    @Override
    public void run() {
        System.out.println("Поток для получения сообщений запущен");
        try {
            while (true){
                if(terminated)return;
                messageConsumer.accept(messageGetter.getMessage());
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
