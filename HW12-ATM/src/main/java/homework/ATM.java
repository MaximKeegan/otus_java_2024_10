package homework;

// Интерфейс для работы с банкоматом (отвечает за принцип единственной ответственности и интерфейс Segregation)
interface ATM {
    void deposit(int value, int count);

    void withdraw(int amount);

    int getBalance();
}