package threads;

import common.Server;
import general.loginorregisterrequest.LoginOrRegisterRequest;
import userprocessing.UserManager;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

public class LoginOrRegisterClientThread extends Thread{
    private final Socket clientSocket;
    private boolean terminated = false;
    private static UserManager userManager;
    public static final int TRIES_LIMIT = 10;
    public LoginOrRegisterClientThread(Socket clientSocket, UserManager userManager)throws NullPointerException{
        LoginOrRegisterClientThread.userManager = userManager;
        if(clientSocket == null)throw new NullPointerException("client is null");
        if(clientSocket.isClosed())throw new RuntimeException("client is closed");
        this.clientSocket = clientSocket;
        start();
    }

    @Override
    public void run() {
        try{
            Server.serverPrint("Поток для регистрации/логина клиента %d запущен\n".formatted( clientSocket.hashCode()));
            ObjectOutputStream objOut = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream objIn = new ObjectInputStream(clientSocket.getInputStream());
            int triesCounter = 0;
            while (!terminated){
                if(triesCounter > TRIES_LIMIT){
                    objOut.writeUTF("TRIES LIMIT EXCEEDED");
                    objOut.flush();
                    terminate();
                    break;
                }
                LoginOrRegisterRequest lor = (LoginOrRegisterRequest)objIn.readObject();
                String userName = lor.getUserName().trim();
                String password = lor.getPassword().trim();
                if(lor.getOperation() != LoginOrRegisterRequest.OperationType.CANCELLED){
                    if(userName.isBlank() || userName.isEmpty()){
                        objOut.writeUTF("USER NAME IS EMPTY");
                        objOut.flush();
                        terminate();
                        break;
                    }
                    if(password.isBlank() || password.isEmpty()){
                        objOut.writeUTF("PASSWORD IS EMPTY");
                        objOut.flush();
                        terminate();
                        break;
                    }
                }
                try{
                    switch (lor.getOperation()){
                        case REGISTER -> {
                            if (!userManager.userIsExist(userName)) {
                                userManager.registerUser(userName, password);
                                Server.addClient(new ClientServerThread(clientSocket, objOut, objIn, userName));
                                Server.serverPrint("Клиент %s зарегистрировался\n".formatted(userName));
                                return;
                            } else {
                                objOut.writeUTF("USER ALREADY EXISTS");
                                objOut.flush();
                                triesCounter++;
                            }
                        }
                        case LOGIN -> {
                            if (userManager.login(userName, password)) {
                                Server.addClient(new ClientServerThread(clientSocket, objOut, objIn, userName));
                                Server.serverPrint("Клиент %s вошёл\n".formatted(userName));
                                return;
                            } else {
                                objOut.writeUTF("INVALID LOGIN OR PASSWORD");
                                objOut.flush();
                                triesCounter++;
                            }
                        }
                        case CANCELLED -> {
                            clientSocket.close();
                            return;
                        }
                    }
                }
                catch (SQLException e){
                    objOut.writeUTF("SERVER HAS DataBase problem");
                    objOut.flush();
                    terminate();
                    throw e;
                }

            }
        }
        catch (EOFException | SocketException e){
            if(!clientSocket.isClosed())e.printStackTrace();
        }
        catch (IOException | SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        finally {
            Server.removeUnregisteredClient(this);
            Server.serverPrint("Поток для регистрации/логина клиента %d завершён\n".formatted( clientSocket.hashCode()));
        }
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
