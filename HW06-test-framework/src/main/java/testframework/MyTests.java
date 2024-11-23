package testframework;
import annotations.*;

public class MyTests {

    @Before
    public void setup() {
        System.out.println("Before test");
    }

    @Test
    public void test1() {
        System.out.println("Test");
    }

    @Test
    public void test2() {
        System.out.println("Test");
        throw new RuntimeException("Test 2 failed!");
    }

    @After
    public void teardown() {
        System.out.println("After test");
    }
}