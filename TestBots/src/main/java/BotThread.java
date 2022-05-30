import general.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
public class BotThread extends Thread{
    private String botName;
    private Socket server;
    private OutputStream serverOut;
    private InputStream serverIn;
    private ObjectOutputStream objectOut;
    private boolean termintated = false;
    public BotThread(String botName, SocketAddress serverAddress) throws IOException {
        this.botName = botName;
        server = new Socket();
        server.connect(serverAddress);
        start();
    }
    @Override
    public void run() {
        try{
            serverOut = server.getOutputStream();
            serverIn = server.getInputStream();
            objectOut = new ObjectOutputStream(serverOut);
            MainBot.cdl.countDown();
            {
                PrintWriter serverPw = new PrintWriter(serverOut, true);
                serverPw.println(botName);
                BufferedReader sIn = new BufferedReader(new InputStreamReader(serverIn));
                System.out.printf("Ожидание ответа от сервера о успешной регистрации бота %s...\n", botName);
                String readyMessage = String.format("WELCOME TO SERVER, %s!", botName);
                String readyMessageFromServer = sIn.readLine();
                int triesCounter = 0;
                while(!readyMessage.equals(readyMessageFromServer)){
                    serverPw.println(botName);
                    readyMessageFromServer = sIn.readLine();
                    if(++triesCounter == 60){
                        System.out.printf("Ожидания ответа от сервера о регистрации бота %s истекло.\n", botName);
                        System.out.printf("Завершение потока бота %s...\n", botName);
                        server.close();
                        return;
                    }
                }
                System.out.printf("Бот %s зарегистрирован!\n", botName);
            }
           objectOut.writeObject(new Message("Hello, i am bot", botName, null));
           for(int i = 0; !termintated; i++){
               objectOut.writeObject(new Message("message " + i, botName, null));
           }
        }
        catch(IOException e){
            if(!termintated){
                e.printStackTrace();
            }
        }
    }
    public void terminate(){
        termintated = true;
        try{
            if(!server.isClosed())server.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
