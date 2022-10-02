package commandinterpretator.exceptions.syntax;

public class InvalidWordsCountInCommandException extends SyntaxErrorException{
    private final int requireWordsCount;

    public InvalidWordsCountInCommandException(int requireWordsCount) {
        super(numberOfInvalidWord);
        this.requireWordsCount = requireWordsCount;
    }

    public int getRequireWordsCount() {
        return requireWordsCount;
    }
}
