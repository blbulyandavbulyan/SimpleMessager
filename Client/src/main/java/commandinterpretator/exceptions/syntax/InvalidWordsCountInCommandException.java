package commandinterpretator.exceptions.syntax;

public class InvalidArgumentsCountException extends SyntaxErrorException{
    private final int requireArgumentsCount;

    public InvalidArgumentsCountException(int requireArgumentsCount) {
        this.requireArgumentsCount = requireArgumentsCount;
    }

    public int getRequireArgumentsCount() {
        return requireArgumentsCount;
    }
}
