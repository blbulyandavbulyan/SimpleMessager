package threads;

import common.Server;
import general.loginorregisterrequest.LoginOrRegisterRequest;
import userprocessing.UserManager;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

public class LoginOrRegisterClientThread extends Thread{
    private Socket clientSocket;
    private boolean terminated = false;
    private static UserManager userManager;
    public static final int TRIES_LIMIT = 10;
    static final PrintStream sPs = Server.getsPs();
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
            sPs.printf("Поток для регистрации/логина клиента %d запущен\n", clientSocket.hashCode());
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
                    if(userName == null || userName.isBlank()){
                        objOut.writeUTF("USER NAME IS EMPTY");
                        objOut.flush();
                        terminate();
                        break;
                    }
                    if(password == null || password.isBlank()){
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
                                sPs.printf("Клиент %s зарегистрировался\n", userName);
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
                                sPs.printf("Клиент %s вошёл\n", userName);
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
            sPs.printf("Поток для регистрации/логина клиента %d завершён\n", clientSocket.hashCode());
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
