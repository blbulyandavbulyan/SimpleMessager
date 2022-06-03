package threads;

import common.Server;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class UnnamedClientThread extends Thread{
    private Socket clientSocket;
    private InputStream clientIn;
    private OutputStream clientOut;
    private static final PrintStream sPs = Server.getsPs();
    boolean terminated = false;
    public UnnamedClientThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        clientIn = clientSocket.getInputStream();
        clientOut = clientSocket.getOutputStream();
        start();
    }
    @Override
    public void run() {
        try{
            PrintWriter cPw = new PrintWriter(clientOut, true);
            BufferedReader sIn = new BufferedReader(new InputStreamReader(clientIn));
            while(!terminated){
                cPw.println("USER ALREADY EXISTS");
                String clientName = sIn.readLine();
                if(!Server.IsClientExists(clientName)){
                    sPs.printf("Пользователь %s зарегистрирован.\n", clientName);
                    Server.addClient(new ClientServerThread(clientSocket, clientName));
                    break;
                }
            }
        }
        catch (SocketException e){
            if(e.getMessage().equals("socket is closed") && !terminated)e.printStackTrace();
        }
        catch(IOException e){
            if(!terminated)e.printStackTrace();
            try{
                if(!clientSocket.isClosed())clientSocket.close();
            }
            catch (IOException e2){
                throw new RuntimeException(e2);
            }
        }
        finally {
            Server.removeUnnamedClient(this);
        }
    }
    public void terminate(){
        terminated = true;
        try {
            if(!clientSocket.isClosed())clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
