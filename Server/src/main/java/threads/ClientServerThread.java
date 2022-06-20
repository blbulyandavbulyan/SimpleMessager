package threads;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

import general.message.Message;
import general.message.textmessage.TextMessage;
import common.Server;
public class ClientServerThread extends Thread{
    private final Socket clientSocket;
    private final ObjectInputStream clientObjIn;
    private final ObjectOutputStream clientObjOut;
    private final String clientName;
    private boolean termintaed = false;//переменная становится true, если был вызван метод terminate()
    public ClientServerThread(Socket clientSocket, ObjectOutputStream clientObjOut, ObjectInputStream clientObjIn, String clientName){
        this.clientSocket = clientSocket;
        this.clientName = clientName;
        this.clientObjIn = clientObjIn;
        this.clientObjOut = clientObjOut;
        start();
    }
    synchronized public void sendMessage(Message msg) throws IOException {
        if(!termintaed){
            clientObjOut.writeObject(msg);
        }
    }
    public void run(){
        try{
            clientObjOut.writeUTF("WELCOME TO SERVER!");
            clientObjOut.flush();
            Server.serverPrint("Поток для клиента %s запущен\n".formatted(clientName));
            while(!termintaed){
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
                        for (ClientServerThread client : Server.getClients()) {
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
            if(!(e.getMessage().equals("Socket closed") && termintaed)){
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
    public void terminate(){
        if(termintaed)return;
        termintaed = true;
        try{
            clientSocket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    private ObjectOutputStream getClientObjOut() {
        return clientObjOut;
    }

    public String getClientName() {
        return clientName;
    }
}
