package Demo;

public class TestLogging implements TestLoggingInterface {

    @Log
    @Override
    public void calculation() { }

    @Log
    @Override
    public void calculation(int param) { }

    @Override
    @Log
    public void calculation(int param1, int param2) { }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) { }

    @Override
    public void noLogMethod() {
        System.out.println("This method is not logged");
    }
}
