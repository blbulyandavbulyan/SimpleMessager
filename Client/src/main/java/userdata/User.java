package userdata;

import java.util.HashSet;

public class User {
    public static final int MAX_USERNAME_LENGTH = 30;
    public static final int MIN_USERNAME_LENGTH = 2;
    public static final HashSet<String> reservedNames = new HashSet<>();
    public static char[] illegalSymbols = new char[]{'@', '!', '\\', '/', '\'', '"', '~', '^', '|', ' '};
    public enum CheckUserNameSteps{USERNAME_IS_CORRECT, USERNAME_RESERVED, INVALID_LENGTH, USERNAME_CONTAINS_ILLEGAL_SYMBOLS}
    static {
        reservedNames.add("ADMIN");
        reservedNames.add("SERVER");
    }
    public static CheckUserNameSteps checkUserName(String userName){
        if(!checkUserNameLength(userName))return CheckUserNameSteps.INVALID_LENGTH;
        if(userNameIsReserved(userName))return CheckUserNameSteps.USERNAME_RESERVED;
        if(userNameContainsIllegalSymbols(userName))return CheckUserNameSteps.USERNAME_CONTAINS_ILLEGAL_SYMBOLS;
        return CheckUserNameSteps.USERNAME_IS_CORRECT;
    }
    public static boolean checkUserNameLength(int length){
        return length >= User.MIN_USERNAME_LENGTH && length <= User.MAX_USERNAME_LENGTH;
    }
    public static boolean userNameIsReserved(String userName){
        return reservedNames.contains(userName);
    }
    public static boolean userNameContainsIllegalSymbols(String userName){
        char[] userNameChars = userName.toCharArray();
        for (char illegalSymbol : illegalSymbols) {
            for (char userNameChar : userNameChars) {
                if(illegalSymbol == userNameChar)return false;
            }
        }
        return true;
    }
    public static boolean checkUserNameLength(String username){
        return checkUserNameLength(username.length());
    }
}
