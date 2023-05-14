package org.blbulyandavbulyan.smclient.serverconnection.interfaces;

import org.blbulyandavbulyan.smgeneral.message.Message;

import java.io.IOException;

public interface MessageGetter extends Closeable {
    Message getMessage() throws IOException, ClassNotFoundException;
}
