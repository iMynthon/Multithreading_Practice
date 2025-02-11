import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Bank {

    private final Logger logger = LogManager.getLogger(Bank.class);

    @Getter
    @EqualsAndHashCode.Include
    private final Map<String, Account> accounts = new HashMap<>();

    @Getter
    private final List<Account> isBlockList = new ArrayList<>();

    private final Random random = new Random();


    public void addAccount(String accountNumber, Account account) {

        accounts.put(accountNumber, account);
    }

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
     * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
     * усмотрение)
     */
    public synchronized void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        if (checkingForBlocking(fromAccountNum, toAccountNum)) {
            if (amount > 50000 && isFraud(fromAccountNum, toAccountNum, amount)) {
                accounts.forEach((key, value) -> {
                    if (key.equals(fromAccountNum) || key.equals(toAccountNum)) {
                        isBlockList.add(value);
                    }
                });
                logger.info(new NotificationOfBlocking("Оба счета: " + fromAccountNum + " " + toAccountNum
                        + " задействованные в транзакции были заблокированы"));
            } else {
                for (Map.Entry<String, Account> account : accounts.entrySet()) {
                    if (account.getKey().equals(fromAccountNum)) {
                        account.getValue().setMoney(account.getValue().getMoney() - amount);
                    }
                    if (account.getKey().equals(toAccountNum)) {
                        account.getValue().setMoney(account.getValue().getMoney() + amount);
                    }
                }
                logger.info("Транзакция успешна завершена деньги отправлены на счет, проверьте баланс");
            }
        }
    }

    public boolean checkingForBlocking(String fromAccountNum, String toAccountNum) {
        boolean checking = true;
        for (Account is : isBlockList) {
            if (is.getAccNumber().equals(fromAccountNum) || is.getAccNumber().equals(toAccountNum)) {
                checking = false;
                logger.info(new NotificationOfBlocking("Аккаунт с номером: " + is.getAccNumber()
                        + " заблокирован службой безопасности за сомнительные переводы"));
            }
        }
        return checking;
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        synchronized (accounts) {
            long amount = 0;
            for (Map.Entry<String, Account> entry : accounts.entrySet()) {
                if (entry.getKey().equals(accountNum)) {
                    amount = entry.getValue().getMoney();
                }
            }
            return amount;
        }
    }

    public long getSumAllAccounts() {
        synchronized (accounts) {
            long sum = 0;
            for (Map.Entry<String, Account> entry : accounts.entrySet()) {
                sum += entry.getValue().getMoney();
            }
            return sum;
        }
    }

}
