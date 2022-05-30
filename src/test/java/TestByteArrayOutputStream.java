import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestByteArrayOutputStream {
    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.in.transferTo(bos);
        for (int i = 0; i < 3; i++) {
            bos.writeTo(System.out);
        }
    }
}
