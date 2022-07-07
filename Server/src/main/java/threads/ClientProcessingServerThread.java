package threads;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

import general.message.Message;
import general.message.textmessage.TextMessage;
import common.Server;
public class ClientProcessingServerThread extends ClientServerThread{
    private final String clientName;

    public ClientProcessingServerThread(ClientServerThread clientServerThread, String clientName){
        super(clientServerThread);
        this.clientName = clientName;
        start();
    }
    synchronized public void sendMessage(Message msg) throws IOException {
        if(!isTerminated()){
            clientObjOut.writeObject(msg);
        }
    }
    public void run(){
        try{
            clientObjOut.writeUTF("WELCOME TO SERVER!");
            clientObjOut.flush();
            Server.serverPrint("Поток для клиента %s запущен\n".formatted(clientName));
            while(!isTerminated()){
                try{
                    Message msg = (Message) clientObjIn.readObject();
                   // Server.printMessage(msg);
                    if(msg.getReceiver()!= null){
                        String msgReceiver = msg.getReceiver();
                        if(!Objects.equals(msgReceiver, clientName)){
                            if(Server.IsClientExists(msgReceiver)) Server.getClient(msgReceiver).sendMessage(msg);
                            else sendMessage(new TextMessage(String.format("Ошибка доставки сообщения, нет такого пользователя %s на сервере!", msgReceiver), "SERVER", clientName));
                        }
                    }
                    else{
                        for (ClientProcessingServerThread client : Server.getClients()) {
                            if(client != this)client.sendMessage(msg);
                        }
                    }
                }
                catch(ClassNotFoundException | ClassCastException e){

                    e.printStackTrace();
                }
            }
        }
        catch (SocketException e){
            if(!(e.getMessage().equals("Socket closed") && isTerminated())){
                e.printStackTrace();
            }
        }
        catch(EOFException  e){
            Server.serverPrint("Пользователь %s отключился.\n".formatted(clientName));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            Server.removeClient(clientName);
        }
    }
    public String getClientName() {
        return clientName;
    }
}
