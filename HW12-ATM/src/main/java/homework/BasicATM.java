package homework;
import java.util.*;

/**
 * Основной класс ATM (реализует принципы SOLID, включая открытость-закрытость для модификаций)
 */
class BasicATM implements ATM {
    private final Map<Integer, CashCell> cashCells = new TreeMap<>(Collections.reverseOrder());

    public BasicATM(List<Integer> values) {
        for (int value : values) {
            cashCells.put(value, new CashCell(value));
        }
    }

    @Override
    public void deposit(int value, int count) {
        CashCell cell = cashCells.get(value);
        if (cell == null) {
            throw new IllegalArgumentException("Неверный номинал: " + value);
        }
        cell.addNotes(count);
    }

    @Override
    public void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше 0.");
        }

        int originalAmount = amount;
        Map<Integer, Integer> withdrawal = new TreeMap<>(Collections.reverseOrder());

        for (Map.Entry<Integer, CashCell> entry : cashCells.entrySet()) {
            int value = entry.getKey();
            CashCell cell = entry.getValue();

            int needed = amount / value;
            if (needed > 0) {
                int withdrawn = cell.removeNotes(needed);
                if (withdrawn > 0) {
                    withdrawal.put(value, withdrawn);
                    amount -= withdrawn * value;
                }
            }
        }

        if (amount > 0) {
            // Откат изменений, если не удалось выдать всю сумму
            for (Map.Entry<Integer, Integer> entry : withdrawal.entrySet()) {
                int value = entry.getKey();
                int returned = entry.getValue();
                cashCells.get(value).addNotes(returned);
            }
            throw new IllegalStateException("Невозможно выдать запрошенную сумму: " + originalAmount);
        }

        System.out.println("Выдано:");
        for (Map.Entry<Integer, Integer> entry : withdrawal.entrySet()) {
            System.out.println(entry.getKey() + " x " + entry.getValue());
        }
    }

    @Override
    public int getBalance() {
        return cashCells.values().stream().mapToInt(CashCell::getTotalAmount).sum();
    }

    public void printState() {
        System.out.println("Состояние банкомата:");
        for (CashCell cell : cashCells.values()) {
            System.out.println(cell.getValue() + " -> " + cell.getCount());
        }
    }
}