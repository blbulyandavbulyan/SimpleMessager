package serverconnection;

import general.loginorregisterrequest.LoginOrRegisterRequest;
import serverconnection.exceptions.*;
import serverconnection.interfaces.MessageGetter;
import serverconnection.interfaces.MessageSender;
import serverconnection.interfaces.StatusMessagePrinter;
import general.message.Message;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

import userdata.*;
public class ServerConnection implements MessageGetter, MessageSender {
    private String userName;
    private final Socket socket;
    private ObjectOutputStream serverObjOut;
    private ObjectInputStream serverObjIn;
    private StatusMessagePrinter statusMessagePrinter;
    private int timeout;
    public ServerConnection(InetSocketAddress serverAddress, int timeout, LoginOrRegisterResultGetter loginOrRegisterResultGetter, StatusMessagePrinter statusMessagePrinter) throws IOException {
        this.statusMessagePrinter = Objects.requireNonNullElseGet(statusMessagePrinter, () -> System.out::println);
        this.timeout = timeout;
        this.statusMessagePrinter.printStatusMessage("Создание сокета...");
        this.socket = new Socket();
        try{
            this.statusMessagePrinter.printStatusMessage("Подключение к серверу...");
            socket.connect(serverAddress, timeout);
            this.statusMessagePrinter.printStatusMessage("Регистрация пользователя...");
            this.statusMessagePrinter.printStatusMessage("Инициализация потока объектов для проведения регистрации/входа и отправки сообщений...");
            initObjStreams();
            loginOrRegister(loginOrRegisterResultGetter);
            //если мы здесь, значит пользователь уже зарегистрирован
            this.statusMessagePrinter.printStatusMessage("Создание завершено.");
        }
        catch(Throwable e) {
            if(!socket.isClosed()) socket.close();
            throw e;
        }

    }
    private  void initObjStreams() throws IOException {
        this.statusMessagePrinter.printStatusMessage("Создание выходного потока объектов...");
        serverObjOut = new ObjectOutputStream(socket.getOutputStream());
        this.statusMessagePrinter.printStatusMessage("Создание выходного потока объектов завершено.");
        this.statusMessagePrinter.printStatusMessage("Создание входного потока объектов...");
        serverObjIn = new ObjectInputStream(socket.getInputStream());
        this.statusMessagePrinter.printStatusMessage("Создание входного потока объектов завершено.");
    }
    private void loginOrRegister(LoginOrRegisterResultGetter loginOrRegisterResultGetter) throws IOException, ServerConnectionException {
        LoginOrRegisterRequest lor = loginOrRegisterResultGetter.getLoginOrRegisterResult(LoginOrRegisterResultGetter.ActionCode.GET);
        if(lor == null)
            throw new RegisterOrLoginInterrupted();
        final String readyMessage = "WELCOME TO SERVER!";
        final String userAlreadyExists = "USER ALREADY EXISTS";
        final String invalidLoginOrPassword = "INVALID LOGIN OR PASSWORD";
        final String registrationOrLoginFailed = "SERVER HAS DataBase problem";
        while(true){
            if(lor == null || lor.getOperation() == LoginOrRegisterRequest.OperationType.CANCELLED)
                throw new RegisterOrLoginInterrupted();
            statusMessagePrinter.printStatusMessage(String.format(
                    "Ожидание ответа от сервера о успешной процедуре %s...",
                    lor.getOperation() == LoginOrRegisterRequest.OperationType.REGISTER ? "регистрации" : "входа"));
            serverObjOut.writeObject(lor);
            String answerFromServer = serverObjIn.readUTF();
            switch (answerFromServer){
                case userAlreadyExists -> lor = loginOrRegisterResultGetter.getLoginOrRegisterResult(LoginOrRegisterResultGetter.ActionCode.GET_NEW_USERNAME_BECAUSE_OLD_IS_ALREADY_REGISTERED);
                case invalidLoginOrPassword -> lor = loginOrRegisterResultGetter.getLoginOrRegisterResult(LoginOrRegisterResultGetter.ActionCode.GET_BECAUSE_INVALID_LOGIN_OR_PASSWORD);

                case readyMessage -> {
                    statusMessagePrinter.printStatusMessage(lor.getOperation() == LoginOrRegisterRequest.OperationType.REGISTER ? "Вы зарегистрированы." : "Вы вошли.");
                    userName = lor.getUserName();
                    return;
                }
                case registrationOrLoginFailed -> throw new ServerHasDBProblemsException();
                default -> throw new WrongAnswerFromServer(answerFromServer);
            }
        }
    }
    public void sendMessage(Message msg) throws IOException, ConnectionClosed{
        if(isClosed())throw new ConnectionClosed();
        serverObjOut.writeObject(msg);
    }
    public Message getMessage() throws IOException, ClassNotFoundException {
        if(isClosed())throw new ConnectionClosed();
        return (Message) serverObjIn.readObject();
    }
    public String getUserName(){
        return userName;
    }
    public boolean isOpen(){
        return !socket.isClosed();
    }
    public boolean isClosed(){
        return socket.isClosed();
    }

    public void close() throws IOException {
        if(!isClosed()){
            socket.close();
        }
    }
    public void reconnect(LoginOrRegisterResultGetter loginOrRegisterResultGetter) throws IOException, ServerConnectionException {
        try{
            this.close();
        }
        catch (IOException ignored){

        }
        socket.connect(socket.getRemoteSocketAddress(), timeout);
        initObjStreams();
        loginOrRegister(loginOrRegisterResultGetter);
        this.statusMessagePrinter.printStatusMessage("Переподключение завершено");

    }
}
