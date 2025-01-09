package ru.keegan.byteCode;

import java.lang.reflect.*;
import java.util.Arrays;

public class LoggingProxy {
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());

                        if (targetMethod.isAnnotationPresent(Log.class)) {
                            StringBuilder logMessage = new StringBuilder("executed method: ")
                                    .append(method.getName());
                            if (args != null && args.length > 0) {
                                logMessage.append(", params: ")
                                        .append(String.join(", ", Arrays.stream(args)
                                                .map(String::valueOf)
                                                .toArray(String[]::new)));
                            }
                            System.out.println(logMessage);
                        }

                        return method.invoke(target, args);
                    }
                }
        );
    }
}
