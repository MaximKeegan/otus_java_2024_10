package Demo;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class LoggingProxy {
    public static Set<String> collectLoggableMethods(Class<?> targetClass) {
        Set<String> loggableMethods = new HashSet<>();
        for (Method method : targetClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Log.class)) {
                loggableMethods.add(getMethodSignature(method));
            }
        }
        return loggableMethods;
    }

    private static String getMethodSignature(Method method) {
        StringBuilder signature = new StringBuilder(method.getName());
        for (Class<?> paramType : method.getParameterTypes()) {
            signature.append("#").append(paramType.getName());
        }
        return signature.toString();
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target, Class<T> interfaceType) {
        Set<String> loggableMethods = LoggingProxy.collectLoggableMethods(target.getClass());

        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String methodSignature = LoggingProxy.getMethodSignature(method);
                        if (loggableMethods.contains(methodSignature)) {
                            logMethodCall(method, args);
                        }
                        return method.invoke(target, args);
                    }

                    private void logMethodCall(Method method, Object[] args) {
                        StringBuilder logMessage = new StringBuilder("executed method: ")
                                .append(method.getName());
                        if (args != null && args.length > 0) {
                            logMessage.append(", params: ")
                                    .append(String.join(", ",
                                            Arrays.stream(args)
                                                    .map(String::valueOf)
                                                    .toArray(String[]::new)));
                        }
                        System.out.println(logMessage);
                    }
                }
        );
    }
}
