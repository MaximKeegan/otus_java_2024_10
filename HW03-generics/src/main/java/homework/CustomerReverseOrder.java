package homework;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    private static Deque<Customer> customers = new ArrayDeque<>();

    public void add(Customer customer) { customers.addLast(customer); }

    public Customer take() { return customers.removeLast(); }
}
