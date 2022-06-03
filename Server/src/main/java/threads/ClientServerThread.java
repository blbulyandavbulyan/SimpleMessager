package threads;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

import general.Message;
import common.Server;
public class ClientServerThread extends Thread{
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String clientName;
    private boolean termintaed = false,//переменная становится true, если был вызван метод terminate()
            inputAndOutputStreamsCreated = false;//переменная становится true, если были созданы объекты для полей in и out
    static final PrintStream sPs = Server.getsPs();
    public ClientServerThread(Socket socket, String clientName){
        this.socket = socket;
        this.clientName = clientName;
        start();
    }
    synchronized public void sendMessage(Message msg) throws IOException {
        if(!termintaed && inputAndOutputStreamsCreated){
            // Возможны проблемы с многопоточностью, не ясно, что будет если два метода вызовут
            out.writeObject(msg);
        }
    }
    public void run(){
        try{
            sPs.printf("Поток для клиента %s запущен\n", clientName);
            {
                PrintWriter cPw = new PrintWriter(socket.getOutputStream(), true);
                cPw.printf("WELCOME TO SERVER, %s!\n", clientName);
            }
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            inputAndOutputStreamsCreated = true;
            while(!termintaed){
                try{
                    Message msg = (Message) in.readObject();
                    Server.printMessage(msg);
                    if(msg.getReceiver()!= null){
                        String msgReceiver = msg.getReceiver();
                        if(!Objects.equals(msgReceiver, clientName)){
                            if(Server.IsClientExists(msgReceiver)) Server.getClient(msgReceiver).sendMessage(msg);
                            else sendMessage(new Message(String.format("Ошибка доставки сообщения, нет такого пользователя %s на сервере!", clientName), "SERVER", clientName));
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
            socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    private ObjectOutputStream getOut() {
        return out;
    }

    public String getClientName() {
        return clientName;
    }
}
