package threads;

import common.Server;
import general.loginorregisterrequest.LoginOrRegisterRequest;
import threads.exceptions.ServerThreadException;
import threads.exceptions.loginorregisterexceptions.UserManagerIsNullException;
import userprocessing.UserManager;
import userprocessing.exceptions.UserAlreadyExistsException;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

public class LoginOrRegisterClientThread extends ClientServerThread{
    private static UserManager userManager;
    public static final int TRIES_LIMIT = 10;
    public LoginOrRegisterClientThread(Socket clientSocket, UserManager userManager)throws ServerThreadException {
        super((clientSocket));
        if(userManager == null)throw new UserManagerIsNullException();
        LoginOrRegisterClientThread.userManager = userManager;
        start();
    }

    @Override
    public void run() {
        try{
            Server.serverPrint("Поток для регистрации/логина клиента %d запущен\n".formatted( clientSocket.hashCode()));
            initObjStreams();
            int triesCounter = 0;
            while (!isTerminated()){
                if(triesCounter > TRIES_LIMIT){
                    clientObjOut.writeUTF("TRIES LIMIT EXCEEDED");
                    clientObjOut.flush();
                    terminate();
                    break;
                }
                LoginOrRegisterRequest lor = (LoginOrRegisterRequest)clientObjIn.readObject();
                String userName = lor.getUserName().trim();
                String password = lor.getPassword().trim();
                if(lor.getOperation() != LoginOrRegisterRequest.OperationType.CANCELLED){
                    if(userName.isBlank() || userName.isEmpty()){
                        clientObjOut.writeUTF("USER NAME IS EMPTY");
                        clientObjOut.flush();
                        terminate();
                        break;
                    }
                    if(password.isBlank() || password.isEmpty()){
                        clientObjOut.writeUTF("PASSWORD IS EMPTY");
                        clientObjOut.flush();
                        terminate();
                        break;
                    }
                }
                try{
                    switch (lor.getOperation()){
                        case REGISTER -> {
                            userManager.registerUser(userName, password);
                            Server.addClient(new ClientProcessingServerThread(this, userName));
                            Server.serverPrint("Клиент %s зарегистрировался\n".formatted(userName));
                            return;
                        }
                        case LOGIN -> {
                            if (userManager.login(userName, password)) {
                                Server.addClient(new ClientProcessingServerThread(this, userName));
                                Server.serverPrint("Клиент %s вошёл\n".formatted(userName));
                                return;
                            } else {
                                clientObjOut.writeUTF("INVALID LOGIN OR PASSWORD");
                                clientObjOut.flush();
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
                    clientObjOut.writeUTF("SERVER HAS DataBase problem");
                    clientObjOut.flush();
                    terminate();
                    throw e;
                }
                catch (UserAlreadyExistsException e){
                    clientObjOut.writeUTF("USER ALREADY EXISTS");
                    clientObjOut.flush();
                    triesCounter++;
                }

            }
        }
        catch (EOFException | SocketException e){
            if(!clientSocket.isClosed())e.printStackTrace();
        }
        catch (IOException | SQLException | ClassNotFoundException e){
            try {
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        finally {
            Server.removeUnregisteredClient(this);
            Server.serverPrint("Поток для регистрации/логина клиента %d завершён\n".formatted( clientSocket.hashCode()));
        }
    }
}
