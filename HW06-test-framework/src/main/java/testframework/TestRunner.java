package testframework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import annotations.*;

public class TestRunner {

    public static void runTests(String className) {
        try {
            Class<?> testClass = Class.forName(className);
            List<Method> beforeMethods = new ArrayList<>();
            List<Method> afterMethods = new ArrayList<>();
            List<Method> testMethods = new ArrayList<>();

            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Before.class)) {
                    beforeMethods.add(method);
                } else if (method.isAnnotationPresent(After.class)) {
                    afterMethods.add(method);
                } else if (method.isAnnotationPresent(Test.class)) {
                    testMethods.add(method);
                }
            }

            int totalTests = testMethods.size();
            int passedTests = 0;
            int failedTests = 0;

            for (Method testMethod : testMethods) {
                Object testInstance = testClass.getDeclaredConstructor().newInstance();
                try {
                    for (Method before : beforeMethods) {
                        before.setAccessible(true);
                        before.invoke(testInstance);
                    }

                    testMethod.setAccessible(true);
                    testMethod.invoke(testInstance);
                    System.out.println("Test passed: " + testMethod.getName());
                    passedTests++;
                } catch (Exception e) {
                    System.out.println("Test failed: " + testMethod.getName() + ". Reason: " + e.getCause());
                    failedTests++;
                } finally {
                    for (Method after : afterMethods) {
                        after.setAccessible(true);
                        try {
                            after.invoke(testInstance);
                        } catch (Exception e) {
                            System.out.println("Error in @After method: " + e.getCause());
                        }
                    }
                }
            }

            System.out.println("Test run summary:");
            System.out.println("Total tests: " + totalTests);
            System.out.println("Passed: " + passedTests);
            System.out.println("Failed: " + failedTests);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}