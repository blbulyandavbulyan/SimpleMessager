package org.blbulyandavbulyan.server.common.exceptions;

public class ServerException extends RuntimeException{
    public ServerException(){

    }
    public ServerException(String msg){
        super(msg);
    }
    public ServerException(Exception e) {
        super(e);
    }
}
