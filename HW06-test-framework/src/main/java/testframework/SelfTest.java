package testframework;
import annotations.*;

public class SelfTest {
    @Before
    public void setup() {
        // Успешный метод Before
    }

    @Test
    public void successfulTest() {
    }

    @Test
    public void failingTest() {
        throw new RuntimeException("Test failed intentionally!");
    }

    @After
    public void teardown() {
        // Успешный метод After
    }
}