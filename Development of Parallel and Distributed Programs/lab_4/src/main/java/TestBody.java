import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestBody {
    private final String testName;
    private final Object[] parameters;
    private final String expectedResult;

    @JsonCreator
    public TestBody(
            @JsonProperty("testName") String testName,
            @JsonProperty("parameters") Object[] parameters,
            @JsonProperty("expectedResult") String expectedResult) {
        this.testName = testName;
        this.parameters = parameters;
        this.expectedResult = expectedResult;
    }

    public String getTestName() {
        return testName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public String getExpectedResult() {
        return expectedResult;
    }
}
