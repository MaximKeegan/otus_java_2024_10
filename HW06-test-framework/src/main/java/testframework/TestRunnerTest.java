package testframework;

import java.util.List;

public class TestRunnerTest {
    public static void main(String[] args) {
        TestResult result = TestRunner.runTests("testframework.SelfTest");

        if (result.getTotalTests() != 2) {
            throw new RuntimeException("Total tests count mismatch");
        }
        if (result.getPassedTests() != 1) {
            throw new RuntimeException("Passed tests count mismatch");
        }
        if (result.getFailedTests() != 1) {
            throw new RuntimeException("Failed tests count mismatch");
        }

        List<String> failedDetails = result.getFailedTestDetails();
        if (!failedDetails.get(0).contains("failingTest")) {
            throw new RuntimeException("Failed test name mismatch");
        }
    }
}