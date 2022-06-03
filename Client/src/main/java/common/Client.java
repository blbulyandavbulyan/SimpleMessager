package common;

import general.Message;
import serverconnection.MessagesReaderThread;
import user.User;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;
public class Client {
//    public static void main(String[] args) throws IOException {
//        Scanner in = new Scanner(System.in);
//        String host  = "localhost";
//        int port = 1234;
//        Server server;
//        try{
//            server = new Server(new InetSocketAddress(host, port), 2*60*1000);
//        }
//        catch(SocketTimeoutException e){
//            System.out.println("Ошибка, истекло время ожидания при подключении к серверу.\nЗавершение...");
//            return;
//        }
//        String myUserName = server.registerUser(in);
//        server.initObjStreams();
//
//        MessagesReaderThread readerThread = new MessagesReaderThread(server, System.out);
//        System.out.println("Ожидаю ввода ваших сообщений...");
//        try{
//            while(true){
//                startpoint:{
//                    String message = in.nextLine();
//                    if(message == null || message.length() == 0){
//                        System.out.println("Ошибка, вы пытаетесь отправить пустое сообщение, попробуйте ещё раз.");
//                        continue;
//                    }
//                    if(message.equals("!EXIT")){
//                        server.close();
//                        readerThread.terminate();
//                        return;
//                    }
//                    String receiverName = User.getReceiverNameFromMessageStr(message);
//                    if(receiverName != null && !User.checkUserNameLength(receiverName.length())){
//                        System.out.printf("Имя получателя должно иметь длинну не меньще %d и не больше %d\n", User.MIN_USERNAME_LENGTH, User.MAX_USERNAME_LENGTH);
//                        System.out.println("Выберите одно из предложенных действий: ");
//                        System.out.println("1)Написать всем\n2)Изменить имя получателя\n3)Написать новое сообщение\n4)Выйти");
//                        System.out.println("Введите номер действия: ");
//                        boolean repeatInput = true;
//                        while (repeatInput){
//                            switch (in.nextInt()){
//                                case 1 -> {
//                                    message = message.replaceAll(receiverName, "");
//                                    receiverName = null;
//                                    repeatInput = false;
//                                }
//                                case 2 -> {
//                                    String newReceiverName = User.readUserName(in, null);
//                                    message = message.replaceFirst(receiverName, newReceiverName);
//                                    receiverName = newReceiverName;
//                                    repeatInput = false;
//                                }
//                                case 3 -> {
//                                    break startpoint;
//                                }
//                                case 4 ->{
//                                    server.close();
//                                    readerThread.terminate();
//                                    return;
//                                }
//                                default -> System.out.println("Нет действия под введённым вами номером, попробуйте ещё раз.");
//                            }
//                        }
//                    }
//                    Message msg = new Message(message, myUserName, receiverName);
//                    server.sendMessage(msg);
//                }
//            }
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//        finally {
//            readerThread.terminate();
//        }
//    }
}
