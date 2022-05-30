public class Pair<PARAMETER_TYPE, EXPECTED_TYPE> {
    PARAMETER_TYPE parameterValue;
    EXPECTED_TYPE expectedValue;
    public Pair(PARAMETER_TYPE parameterValue, EXPECTED_TYPE expectedValue){
        this.expectedValue = expectedValue;
        this.parameterValue = parameterValue;
    }
}
