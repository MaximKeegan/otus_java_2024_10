package homework;

import java.util.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ATMTest {

    // Все тесты должны проходить, менять тесты не надо.

    @Test
    @DisplayName("Проверяем, что все работает")
    void setterCustomerTest() {
        // given
        BasicATM atm = new BasicATM(Arrays.asList(50, 100, 500, 1000));

        atm.deposit(100, 10);
        atm.deposit(500, 5);
        atm.deposit(50, 20);

        atm.withdraw(1150);
        atm.printState();

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            atm.withdraw(10);
        });
        assertEquals("Невозможно выдать запрошенную сумму: 10", exception.getMessage());

        System.out.println("Баланс: " + atm.getBalance());

        exception = assertThrows(IllegalStateException.class, () -> {
            atm.withdraw(5000);
        });
        assertEquals("Невозможно выдать запрошенную сумму: 5000", exception.getMessage());
    }
}
