package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.AbstractMap;

public class CustomerService {

    private static Comparator<Customer> byScores = (c1, c2) -> Long.compare(c1.getScores(), c2.getScores());
    private static TreeMap <Customer, String> customers = new TreeMap<>(byScores);

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> smallestEntry = customers.firstEntry();
        if (smallestEntry != null) {
            Customer customerCopy = new Customer(smallestEntry.getKey());
            return new AbstractMap.SimpleEntry<>(customerCopy, smallestEntry.getValue());
        }
        return null;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextEntry = customers.higherEntry(customer);
        if (nextEntry != null) {
            Customer customerCopy = new Customer(nextEntry.getKey());
            return new AbstractMap.SimpleEntry<>(customerCopy, nextEntry.getValue());
        }
        return null;
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
