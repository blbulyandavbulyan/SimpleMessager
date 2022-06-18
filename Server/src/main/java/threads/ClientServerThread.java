package threads;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

import general.message.Message;
import general.message.textmessage.TextMessage;
import common.Server;
public class ClientServerThread extends Thread{
    private Socket clientSocket;
    private ObjectInputStream clientObjIn;
    private ObjectOutputStream clientObjOut;
    private String clientName;
    private boolean termintaed = false,//переменная становится true, если был вызван метод terminate()
            inputAndOutputStreamsCreated = false;//переменная становится true, если были созданы объекты для полей in и out
    static final PrintStream sPs = Server.getsPs();
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
            sPs.printf("Поток для клиента %s запущен\n", clientName);
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
            sPs.printf("Пользователь %s отключился.\n", clientName);
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
