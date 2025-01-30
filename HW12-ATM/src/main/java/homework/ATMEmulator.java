package homework;
import java.util.*;

public class ATMEmulator {
    public static void main(String[] args) {
        BasicATM atm = new BasicATM(Arrays.asList(50, 100, 500, 1000));

        atm.printState();

        System.out.println("Баланс: " + atm.getBalance());

        atm.deposit(100, 10);
        atm.deposit(500, 5);
        atm.deposit(50, 20);

        atm.printState();

        System.out.println("Баланс: " + atm.getBalance());

        try {
            atm.withdraw(1150);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        atm.printState();

        System.out.println("Баланс: " + atm.getBalance());
    }
}