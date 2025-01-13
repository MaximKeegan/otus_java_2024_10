package Demo;

public class Demo {
    public static void main(String[] args) {
        var original = new TestLogging();

        var proxy = LoggingProxy.createProxy(original, TestLoggingInterface.class);

        proxy.calculation();
        proxy.calculation(6);
        proxy.calculation(3, 4);
        proxy.calculation(1, 2, "test");
        proxy.noLogMethod();
    }
}
