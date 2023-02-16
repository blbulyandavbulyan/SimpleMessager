package commandprocessing.exceptions;

import common.exceptions.ServerException;

public class CommandProcessingException extends ServerException {
    public CommandProcessingException(String msg) {
        super(msg);
    }
    public CommandProcessingException(){

    }
}
