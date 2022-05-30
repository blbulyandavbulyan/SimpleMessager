import general.Message;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static OutputStream serverOut;
    private static InputStream serverIn;
    private Socket serverSocket;
    private ObjectOutputStream serverObjOut;
    private ObjectInputStream serverObjIn;
    public Server(InetSocketAddress addr, int timeout) throws IOException {
        System.out.println("Создание сокета...");
        serverSocket = new Socket();
        System.out.println("Подключение к серверу...");
        serverSocket.connect(addr, timeout);
        System.out.println("Получение выходного потока от сокета");
        serverOut = serverSocket.getOutputStream();
        System.out.println("Получение входного потока от сокета...");
        serverIn = serverSocket.getInputStream();
        System.out.println("Объект для подключения к серверу создан...");

    }
    public void initObjStreams() throws IOException {
        System.out.println("Создание выходного потока объектов...");
        serverObjOut = new ObjectOutputStream(serverOut);
        System.out.println("Создание выходного потока объектов завершено.");
        System.out.println("Создание входного потока объектов...");
        serverObjIn = new ObjectInputStream(serverIn);
        System.out.println("Создание входного потока объектов завершено.");
    }
    public String registerUser(Scanner in) throws IOException {
        PrintWriter toServer = new PrintWriter(serverOut, true);
        BufferedReader sIn = new BufferedReader(new InputStreamReader(serverIn));
        int triesCounter = 0;
        String userName = User.readUserName(in);
        while(true){
            toServer.println(userName);
            //ожидание ответа от сервера:
            System.out.println("Ожидание ответа от сервера о успешной регистрации...");
            String answerFromServer = sIn.readLine();
            {
                final String readyMessage =  String.format("WELCOME TO SERVER, %s!", userName);
                final String userAlreadyExists = "USER ALREADY EXISTS";
                if(answerFromServer.equals(readyMessage)){
                    System.out.println("Вы зарегистрированы!");
                    break;
                }
                else if(answerFromServer.equals(userAlreadyExists)){
                    System.out.println("Введённое вами имя пользователя уже существует, введите другое.");
                    userName = User.readUserName(in);
                }
                else{
                    System.out.println("Неожиданный ответ от сервера, повторная попытка регистрации...");
                    if(++triesCounter == 60){
                        System.out.println("Ожидания ответа от сервера истекло.");
                        System.out.println("Завершение...");
                        serverSocket.close();
                        return null;
                    }
                }
            }
        }
        return userName;
    }
    public Message readMessage() throws IOException, ClassNotFoundException {
        return (Message) serverObjIn.readObject();
    }
    public void sendMessage(Message msg) throws IOException {
        serverObjOut.writeObject(msg);
    }
    public void close() throws IOException {
        if(!serverSocket.isClosed())serverSocket.close();
    }
    public boolean isClosed(){
        return serverSocket.isClosed();
    }
}
