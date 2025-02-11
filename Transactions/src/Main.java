import java.util.Random;

public class Main {

    private static final Random random = new Random();

    private static final String[] letters = {"А", "В", "Е", "К", "М", "Н", "О", "Р", "С", "Т", "У", "Х"};

    private static final Bank T_BANK = new Bank();

    public static void main(String[] args) {

        String accountNumber = letterGeneration();
        T_BANK.addAccount(accountNumber, new Account(Math.abs(random.nextInt()), accountNumber));
        String accountNumber1 = letterGeneration();
        T_BANK.addAccount(accountNumber1, new Account(Math.abs(random.nextInt()), accountNumber1));
        String accountNumber2 = letterGeneration();
        T_BANK.addAccount(accountNumber2, new Account(Math.abs(random.nextInt()), accountNumber2));
        String accountNumber3 = letterGeneration();
        T_BANK.addAccount(accountNumber3, new Account(Math.abs(random.nextInt()), accountNumber3));
        String accountNumber4 = letterGeneration();
        T_BANK.addAccount(accountNumber4, new Account(Math.abs(random.nextInt()), accountNumber4));
        String accountNumber5 = letterGeneration();
        T_BANK.addAccount(accountNumber5, new Account(Math.abs(random.nextInt()), accountNumber5));

        System.out.println("Баланс счета: " + accountNumber + " - " + T_BANK.getBalance(accountNumber));
        System.out.println("Баланс счета: " + accountNumber1 + " - " + T_BANK.getBalance(accountNumber1));
        System.out.println("Баланс счета: " + accountNumber2 + " - " + T_BANK.getBalance(accountNumber2));
        System.out.println("Баланс счета: " + accountNumber3 + " - " + T_BANK.getBalance(accountNumber3));
        System.out.println("Баланс счета: " + accountNumber4 + " - " + T_BANK.getBalance(accountNumber4));
        System.out.println("Баланс счета: " + accountNumber5 + " - " + T_BANK.getBalance(accountNumber5) + "\t\n");


        System.out.println("Общая сумма денег в банке: " + T_BANK.getSumAllAccounts() + "\t\n");


        multithreadingTransaction(accountNumber, accountNumber1, 50000);

        multithreadingTransaction(accountNumber2, accountNumber3, 40000);

        multithreadingTransaction(accountNumber4, accountNumber5, 60000);

        multithreadingTransaction(accountNumber4, accountNumber1, 60000);

        multithreadingTransaction(accountNumber, accountNumber3, 60000);


    }

    public static void multithreadingTransaction(String fromAccNumber, String toAccNumbers, long amount) {
        synchronized (T_BANK) {
            Thread thread = new Thread(() -> {
                try {
                    T_BANK.transfer(fromAccNumber, toAccNumbers, amount);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Баланс счета отправителя: " + fromAccNumber + " - " + T_BANK.getBalance(fromAccNumber)
                        + "\t\nБаланс счета получателя: " + toAccNumbers + " - " + T_BANK.getBalance(toAccNumbers));
                System.out.println("Общая сумма денег в банке: " + T_BANK.getSumAllAccounts());
            });
            thread.start();
        }
    }

    public static String letterGeneration() {
        StringBuilder accNumbers = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int letter = (int) (Math.random() * (letters.length - 1));
            int number = (int) (Math.random() * 10);
            accNumbers.append(letters[letter].repeat(2)).append(number);
        }
        return accNumbers.toString();
    }

}
