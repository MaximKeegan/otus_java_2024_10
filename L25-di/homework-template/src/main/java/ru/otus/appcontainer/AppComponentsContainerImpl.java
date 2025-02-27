package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.util.*;
import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(List.of(initialConfigClass));
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        if (configClasses.isEmpty()) {
            throw new IllegalArgumentException("No configuration classes found in package: " + packageName);
        }
        processConfig(new ArrayList<>(configClasses));
    }

    private void processConfig(List<Class<?>> configClasses) {
        for (Class<?> configClass : configClasses) {
            checkConfigClass(configClass);
            try {
                Object configInstance = configClass.getDeclaredConstructor().newInstance();

                Method[] methods = configClass.getDeclaredMethods();

                List<Method> componentMethods = new ArrayList<>();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(AppComponent.class)) {
                        componentMethods.add(method);
                    }
                }

                componentMethods.sort(Comparator.comparingInt(
                        m -> m.getAnnotation(AppComponent.class).order()));

                for (Method method : componentMethods) {
                    AppComponent annotation = method.getAnnotation(AppComponent.class);
                    String name = annotation.name();

                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object[] parameters = new Object[parameterTypes.length];
                    for (int i = 0; i < parameterTypes.length; i++) {
                        parameters[i] = getAppComponent(parameterTypes[i]);
                        if (parameters[i] == null) {
                            throw new RuntimeException("Dependency not found for type: " + parameterTypes[i].getName());
                        }
                    }

                    Object component = method.invoke(configInstance, parameters);

                    appComponents.add(component);
                    if (appComponentsByName.containsKey(name)) {
                        throw new RuntimeException("Component with name '" + name + "' already exists");
                    }
                    appComponentsByName.put(name, component);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to process configuration: " + e.getMessage(), e);
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> matchingComponents = new ArrayList<>();
        for (Object component : appComponents) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                matchingComponents.add(component);
            }
        }
        if (matchingComponents.isEmpty()) {
            throw new RuntimeException("No component found for type: " + componentClass.getName());
        }
        if (matchingComponents.size() > 1) {
            throw new RuntimeException("More than one component found for type: " + componentClass.getName());
        }
        return componentClass.cast(matchingComponents.getFirst());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName);
        if (component == null) {
            throw new RuntimeException("Component not found for name: " + componentName);
        }
        return (C) component;
    }
}
