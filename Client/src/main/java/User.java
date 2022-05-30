import java.util.HashSet;
import java.util.Scanner;

public class User {
    public static final int MAX_USERNAME_LENGTH = 30;
    public static final int MIN_USERNAME_LENGTH = 2;
    public static final HashSet<String> reservedNames = new HashSet<>();
    static {
        reservedNames.add("ADMIN");
        reservedNames.add("SERVER");
    }
    public static boolean checkUserName(String username){
        return checkUserNameLength(username);
    }
    public static boolean checkUserNameLength(int length){
        return !(length >= User.MIN_USERNAME_LENGTH && length <= User.MAX_USERNAME_LENGTH);
    }
    public static boolean checkUserNameLength(String username){
        if(checkUserNameLength(username.length())){
            System.out.printf("Ошибка, имя пользователя должно быть не меньше %d и не больше %d, попробуйте ещё раз.\n", MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH);
            return false;
        }

        return true;
    }
    public static String readUserName(Scanner in, HashSet<String> reservedNames){
        String userName;
        while (true){
            System.out.print("Введите имя пользователя: ");
            userName = in.nextLine();
            if(reservedNames != null){
                if(reservedNames.contains(userName)){
                    System.out.println("Имя пользователя не должно совпадать с ни одним из списка: ");
                    for (String reservedName : reservedNames) {
                        System.out.printf("%s ", reservedName);
                    }
                    System.out.println();
                }
            }
            if(userName.equals("SERVER") || userName.equals("ADMIN")){
                System.out.println("Имя пользователя не должно совпадать с ни одним из списка: SERVER, ADMIN.");
                continue;
            }
            if(!User.checkUserName(userName))continue;
            break;
        }
        return userName;
    }
    public static String readUserName(Scanner in){
        return readUserName(in, User.reservedNames);
    }
    public static String getReceiverNameFromMessageStr(String msg){
        String receiverName = null;
        char [] msgChars = msg.toCharArray();
        if(msgChars[0] == '@'){
            int i = 0;
            for(;msgChars[i] != ',' && i < msgChars.length; i++);
            if(checkUserNameLength(i-1))
            receiverName = String.valueOf(msgChars, 1, i-1);
        }
        return receiverName;
    }
}
