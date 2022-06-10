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
        byte a = -29;
        int b = -29 & 0xff;
        System.out.println(b);
    }
}
