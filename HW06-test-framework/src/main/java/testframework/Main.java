package testframework;

import static testframework.TestRunner.*;

public class Main {
    public static void main(String[] args) {
        TestResult result = TestRunner.runTests("testframework.MyTests");

        System.out.println("Test run summary:");
        System.out.println("Total tests: " + result.getTotalTests());
        System.out.println("Passed: " + result.getPassedTests());
        System.out.println("Failed: " + result.getFailedTests());

        if (result.getFailedTests() > 0) {
            System.out.println("Failed test details:");
            for (String detail : result.getFailedTestDetails()) {
                System.out.println(" - " + detail);
            }
        }
    }
}