package testframework;

import java.util.List;

public class TestRunnerTest {
    public static void main(String[] args) {
        TestResult result = TestRunner.runTests("testframework.SelfTest");

        assert result.getTotalTests() == 2 : "Total tests count mismatch";
        assert result.getPassedTests() == 1 : "Passed tests count mismatch";
        assert result.getFailedTests() == 1 : "Failed tests count mismatch";

        List<String> failedDetails = result.getFailedTestDetails();
        assert failedDetails.size() == 1 : "Failed test details count mismatch";
        assert failedDetails.get(0).contains("failingTest") : "Failed test name mismatch";
    }
}