package org.blbulyandavbulyan.smgeneral.message.servermessages;

import org.blbulyandavbulyan.smgeneral.message.Message;

import java.io.Serial;

abstract class ServerMessage extends Message {
    @Serial
    private static final long serialVersionUID = -5305409982587304522L;
    protected ServerMessage(String receiver){
        super("SERVER", receiver);
    }

}

