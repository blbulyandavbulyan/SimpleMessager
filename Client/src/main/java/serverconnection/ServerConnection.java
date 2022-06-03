package serverconnection;

import common.StatusMessagePrinter;
import general.Message;
import serverconnection.exceptions.ConnectionClosed;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.Callable;

import serverconnection.exceptions.RegisterUserInterrupted;
import serverconnection.exceptions.WrongAnswerFromServer;
import user.*;
public class ServerConnection implements Closeable {
    private String userName;
    private final Socket serverSocket;
    private ObjectOutputStream serverObjOut;
    private ObjectInputStream serverObjIn;
    private StatusMessagePrinter statusMessagePrinter;
    public ServerConnection(InetSocketAddress serverAddress, int timeout, UserNameGetter userNameGetter, StatusMessagePrinter statusMessagePrinter) throws IOException {
        this.statusMessagePrinter = Objects.requireNonNullElseGet(statusMessagePrinter, () -> System.out::println);
        this.statusMessagePrinter.printStatusMessage("Создание сокета...");
        this.serverSocket = new Socket();
        try{
            this.statusMessagePrinter.printStatusMessage("Подключение к серверу...");
            serverSocket.connect(serverAddress, timeout);
            this.statusMessagePrinter.printStatusMessage("Регистрация пользователя...");
            registerUser(userNameGetter);
            //если мы здесь, значит пользователь уже зарегистрирован
            this.statusMessagePrinter.printStatusMessage("Инициализация потоков объектов для получения и отправки сообщений...");
            initObjStreams();
            this.statusMessagePrinter.printStatusMessage("Создание завершено.");
        }
        catch(Throwable e) {
            if(!serverSocket.isClosed())serverSocket.close();
            throw e;
        }

    }
    private void initObjStreams() throws IOException {
        statusMessagePrinter.printStatusMessage("Создание выходного потока объектов...");
        serverObjOut = new ObjectOutputStream(serverSocket.getOutputStream());
        statusMessagePrinter.printStatusMessage("Создание выходного потока объектов завершено.");
        statusMessagePrinter.printStatusMessage("Создание входного потока объектов...");
        serverObjIn = new ObjectInputStream(serverSocket.getInputStream());
        statusMessagePrinter.printStatusMessage("Создание входного потока объектов завершено.");
    }
    private static String getUserName(UserNameGetter getUserName, UserNameGetter.ActionCode actionCode){
        String userName = getUserName.getUserName(actionCode, null);
        User.CheckUserNameSteps checkUserNameStep = User.checkUserName(userName);
        while(checkUserNameStep != User.CheckUserNameSteps.USERNAME_IS_CORRECT) {
            userName = getUserName.getUserName(UserNameGetter.ActionCode.GET_NEW_USER_NAME_BECAUSE_OLD_HAS_INVALID_LENGTH, checkUserNameStep);
            checkUserNameStep = User.checkUserName(userName);
        }
        return userName;
    }
    private void registerUser(UserNameGetter userNameGetter) throws IOException, WrongAnswerFromServer, RegisterUserInterrupted{
        PrintWriter toServer = new PrintWriter(serverSocket.getOutputStream(), true);
        BufferedReader sIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        String userName = getUserName(userNameGetter, UserNameGetter.ActionCode.GET_USERNAME);
        while(true){
            if(userName == null)
                throw new RegisterUserInterrupted("Регистрация прервана пользователем, подключение не будет создано.");
            toServer.println(userName);
            //ожидание ответа от сервера:
            userNameGetter.printStatusMessage("Ожидание ответа от сервера о успешной регистрации...");
            String answerFromServer = sIn.readLine();
            {
                final String readyMessage =  String.format("WELCOME TO SERVER, %s!", userName);
                final String userAlreadyExists = "USER ALREADY EXISTS";
                if(answerFromServer.equals(readyMessage))break;
                else if(answerFromServer.equals(userAlreadyExists)){
                    userName = getUserName(userNameGetter, UserNameGetter.ActionCode.GET_NEW_USERNAME_BECAUSE_OLD_IS_ALREADY_REGISTERED);
                }
                else throw new WrongAnswerFromServer("Неожиданный ответ от сервера при регистрации, регистрация прерванна.");
            }
        }
        this.userName = userName;
    }
    public void sendMessage(Message msg) throws IOException{
        if(isClosed())throw new ConnectionClosed("Ошибка отправки сообщения, подключение закрыто.");
        serverObjOut.writeObject(msg);
    }
    public Message getMessage() throws IOException, ClassNotFoundException {
        if(isClosed())throw new ConnectionClosed("Ошибка получения сообщения, подключение закрыто.");
        return (Message) serverObjIn.readObject();
    }
    public String getUserName(){
        return userName;
    }
    public boolean isOpen(){
        return !serverSocket.isClosed();
    }
    public boolean isClosed(){
        return serverSocket.isClosed();
    }

    @Override
    public void close() throws IOException {
        if(!isClosed()){
            serverSocket.close();
        }
    }
}
