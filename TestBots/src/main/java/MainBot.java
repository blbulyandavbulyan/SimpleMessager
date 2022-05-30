//это бот, который имитирует работу клиента, его задача писать сообщения, принимать сообщения он не должен
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MainBot {
    static Set<BotThread> botThreads = new HashSet<>();
    static CountDownLatch cdl;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Ввведите количество ботов-потоков:");
        int n = in.nextInt();
        String host  = "localhost";
        int port = 1234;
        cdl = new CountDownLatch(n);
        for (int i = 0; i < n; i++){
            try {
                botThreads.add(new BotThread("testBot" + i, new InetSocketAddress(host, port)));
            } catch (IOException e) {
                System.err.println("Не удалось создать бота под номером " + i);
                e.printStackTrace();
            }
        }
        try {
            if(cdl.await(2, TimeUnit.MINUTES))System.out.println("Процедура создания ботов закончена.");
            else System.out.println("Что-то пошло не так...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
