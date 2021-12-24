import com.fasterxml.jackson.annotation.JsonProperty;

public class TestResult {
    private final String status;
    private final String testName;
    private final String expectedResult;
    private final String receivedResult;

    @JsonProperty
    public TestResult(
            @JsonProperty("status") String status,
            @JsonProperty("testName") String testName,
            @JsonProperty("expectedResult") String expectedResult,
            @JsonProperty("receivedResult") String receivedResult) {
        this.status = status;
        this.testName = testName;
        this.expectedResult = expectedResult;
        this.receivedResult = receivedResult;
    }
}
