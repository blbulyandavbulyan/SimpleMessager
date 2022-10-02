package serverconnection.exceptions;

public class WrongAnswerFromServer extends ServerConnectionException{
    private final String answerFromServer;
    public WrongAnswerFromServer(String answerFromServer) {
        this.answerFromServer = answerFromServer;
    }

    public String getAnswerFromServer() {
        return answerFromServer;
    }
}
