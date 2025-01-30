package homework;

// Класс для хранения банкнот (отвечает за принцип единственной ответственности)
class CashCell {
    private final int value;
    private int count;

    public CashCell(int value) {
        this.value = value;
        this.count = 0;
    }

    public int getValue() {
        return value;
    }

    public int getCount() {
        return count;
    }

    public void addNotes(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количество банкнот должно быть больше 0.");
        }
        this.count += count;
    }

    public int removeNotes(int needed) {
        int removed = Math.min(needed, count);
        count -= removed;
        return removed;
    }

    public int getTotalAmount() {
        return value * count;
    }
}