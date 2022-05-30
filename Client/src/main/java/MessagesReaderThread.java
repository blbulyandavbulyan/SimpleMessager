import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class MessagesReaderThread extends Thread{
    private Server server;
    private PrintStream ps;
    private boolean terminated = false;
    public MessagesReaderThread(Server server, PrintStream ps){
        this.server = server;
        this.ps = ps;
        start();
    }
    @Override
    public void run() {
        System.out.println("Поток для получения сообщений запущен");
        try {
            while (true){
                if(terminated)return;
                ps.println(server.readMessage());
            }
        }
        catch (SocketException e){
            if(server.isClosed() && terminated){
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
