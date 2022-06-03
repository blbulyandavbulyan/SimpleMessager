package serverconnection.exceptions;

import java.io.IOException;

public class ConnectionClosed extends IOException {
    public ConnectionClosed(String msg){
        super(msg);
    }
}
