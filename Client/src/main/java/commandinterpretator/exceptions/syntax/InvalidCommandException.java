package commandinterpretator.exceptions.syntax;

public class InvalidCommandException extends SyntaxErrorException {
    public InvalidCommandException() {
        super(numberOfInvalidWord);
    }
}
