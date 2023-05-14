package org.blbulyandavbulyan.smserver.threads;

import org.blbulyandavbulyan.smserver.common.Server;
import org.blbulyandavbulyan.smserver.interfaces.ServerLogger;
import org.blbulyandavbulyan.smserver.threads.exceptions.clientserverthread.ClientSocketIsClosedException;
import org.blbulyandavbulyan.smserver.threads.exceptions.clientserverthread.ClientSocketIsNullException;
import org.blbulyandavbulyan.smserver.threads.exceptions.clientserverthread.ClientThreadIsTerminatedException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class ClientServerThread extends Thread{
    protected final Server server;
    protected final ServerLogger serverLogger;
    protected final Socket clientSocket;
    protected ObjectInputStream clientObjIn;
    protected ObjectOutputStream clientObjOut;
    private boolean terminated = false;//переменная становится true, если был вызван метод terminate();
    private boolean objStreamInited = false;
    protected ClientServerThread(Socket clientSocket, Server server, ServerLogger serverLogger) {
        if(clientSocket == null)throw new ClientSocketIsNullException();
        if(clientSocket.isClosed())throw new ClientSocketIsClosedException();
        this.clientSocket = clientSocket;
        this.server = server;
        this.serverLogger = serverLogger;
    }
    protected ClientServerThread(ClientServerThread clientServerThread){
        if(clientServerThread.terminated)throw new ClientThreadIsTerminatedException();
        clientSocket = clientServerThread.clientSocket;
        clientObjIn = clientServerThread.clientObjIn;
        clientObjOut = clientServerThread.clientObjOut;
        objStreamInited = clientServerThread.objStreamInited;
        this.server = clientServerThread.server;
        this.serverLogger = clientServerThread.serverLogger;
    }
    protected void initObjStreams() throws IOException {
        if(!objStreamInited){
            clientObjOut = new ObjectOutputStream(clientSocket.getOutputStream());
            clientObjIn = new ObjectInputStream(clientSocket.getInputStream());
            objStreamInited = true;
        }
    }
    public boolean isTerminated(){
        return terminated;
    }

    public void terminate(){
        terminated = true;
        if(!clientSocket.isClosed()) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
