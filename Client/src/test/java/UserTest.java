import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @org.junit.jupiter.api.Test
    void checkUserName() {
    }

    @org.junit.jupiter.api.Test
    void checkUserNameLength() {
    }

    @org.junit.jupiter.api.Test
    void testCheckUserNameLength() {
    }

    @org.junit.jupiter.api.Test
    void readUserName() {
    }

    @org.junit.jupiter.api.Test
    void testReadUserName() {
    }

    @org.junit.jupiter.api.Test
    void getReceiverNameFromMessageStr() {

        Pair<String, String>[] testData = new Pair[]{
                new Pair("@david, Hi, I am johan", "david"),
                new Pair("@jh, hello", "jh"),
                new Pair("@genadiy, hello", "genadiy"),
                new Pair("@georgiy, hello", "georgiy"),
                new Pair("@julia, hello", "julia"),
                new Pair("@martin, hello", "martin"),
                new Pair("@Marastine Geor, hello", "Marastine Geor")
        };
        for (var p : testData) {
            assertEquals(p.expectedValue, User.getReceiverNameFromMessageStr(p.parameterValue));
        }
    }
}