package commandinterpretator.exceptions.syntax;

import commandinterpretator.exceptions.CommandInterpreterException;

public class InvalidWordsCountInCommandException extends CommandInterpreterException {
    private final int requireWordsCount;

    public InvalidWordsCountInCommandException(int requireWordsCount) {
        this.requireWordsCount = requireWordsCount;
    }

    public int getRequireWordsCount() {
        return requireWordsCount;
    }
}
