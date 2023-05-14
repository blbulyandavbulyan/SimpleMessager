package org.blbulyandavbulyan.server.commandprocessing.exceptions;

public class ExecutionError extends CommandProcessingException{
    public ExecutionError(Exception e){
        super(e);
    }

}
