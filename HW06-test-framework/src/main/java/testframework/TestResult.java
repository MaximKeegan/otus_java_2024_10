package testframework;

import java.util.ArrayList;
import java.util.List;

public class TestResult {
    private int totalTests;
    private int passedTests;
    private int failedTests;
    private final List<String> failedTestDetails = new ArrayList<>();

    public void addTestResult(boolean isSuccess, String testName, Throwable error) {
        totalTests++;
        if (isSuccess) {
            passedTests++;
        } else {
            failedTests++;
            failedTestDetails.add(testName + ": " + (error != null ? error.getMessage() : "Unknown error"));
        }
    }

    public int getTotalTests() {
        return totalTests;
    }

    public int getPassedTests() {
        return passedTests;
    }

    public int getFailedTests() {
        return failedTests;
    }

    public List<String> getFailedTestDetails() {
        return failedTestDetails;
    }
}