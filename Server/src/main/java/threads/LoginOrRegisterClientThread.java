package threads;

import common.Server;
import general.loginorregisterrequest.LoginOrRegisterRequest;
import loginandregister.LoginAndRegisterUserInterface;
import threads.exceptions.ServerThreadException;
import loginandregister.exceptions.UserAlreadyExistsException;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

public class LoginOrRegisterClientThread extends ClientServerThread{
    public static final int TRIES_LIMIT = 10;
    public LoginOrRegisterClientThread(Socket clientSocket, Server server)throws ServerThreadException {
        super(clientSocket, server);
        start();
    }

    @Override
    public void run() {
        try{
            LoginAndRegisterUserInterface loginAndRegisterUserInterface = server.getLoginOrRegisterUser();
            server.print("Поток для регистрации/логина клиента %d запущен\n".formatted( clientSocket.hashCode()));
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
                            loginAndRegisterUserInterface.register(userName, password);
                            server.addClient(new ClientProcessingServerThread(this, userName));
                            server.print("Клиент %s зарегистрировался\n".formatted(userName));
                            return;
                        }
                        case LOGIN -> {
                            if (loginAndRegisterUserInterface.login(userName, password)) {
                                server.addClient(new ClientProcessingServerThread(this, userName));
                                server.print("Клиент %s вошёл\n".formatted(userName));
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
//                catch (SQLException e){
//                    clientObjOut.writeUTF("SERVER HAS DataBase problem");
//                    clientObjOut.flush();
//                    terminate();
//                    throw e;
//                }
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
        catch (IOException | ClassNotFoundException e){
            try {
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        finally {
            server.removeUnregisteredClient(this);
            server.print("Поток для регистрации/логина клиента %d завершён\n".formatted( clientSocket.hashCode()));
        }
    }
}
