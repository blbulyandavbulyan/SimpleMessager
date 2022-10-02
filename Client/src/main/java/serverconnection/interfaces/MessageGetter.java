package serverconnection.interfaces;

import general.message.Message;

import java.io.IOException;

public interface MessageGetter extends Closeable {
    Message getMessage() throws IOException, ClassNotFoundException;
}
