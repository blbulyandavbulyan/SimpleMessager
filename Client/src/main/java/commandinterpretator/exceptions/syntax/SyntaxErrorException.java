package commandinterpretator.exceptions.syntax;

import commandinterpretator.exceptions.CommandInterpreterException;

public class SyntaxErrorException extends CommandInterpreterException {
    private final int positionInSplittingCommandArray;

    public SyntaxErrorException(int positionInSplittingCommandArray) {
        this.positionInSplittingCommandArray = positionInSplittingCommandArray;
    }

    public int getPositionInSplittingCommandArray() {
        return positionInSplittingCommandArray;
    }
    public int getNumberOfInvalidArgument(){
        return positionInSplittingCommandArray+1;
    }
}
