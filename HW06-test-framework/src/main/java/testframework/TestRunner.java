package testframework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import annotations.*;


public class TestRunner {
    public static TestResult runTests(String className) {
        TestResult result = new TestResult();

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

            for (Method testMethod : testMethods) {
                Object testInstance = testClass.getDeclaredConstructor().newInstance();
                boolean success = true;
                Throwable testError = null;

                try {
                    for (Method before : beforeMethods) {
                        before.setAccessible(true);
                        before.invoke(testInstance);
                    }

                    testMethod.setAccessible(true);
                    testMethod.invoke(testInstance);
                } catch (Throwable t) {
                    success = false;
                    testError = t.getCause() != null ? t.getCause() : t;
                } finally {
                    for (Method after : afterMethods) {
                        try {
                            after.setAccessible(true);
                            after.invoke(testInstance);
                        } catch (Exception ignored) {
                        }
                    }
                }

                result.addTestResult(success, testMethod.getName(), testError);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}