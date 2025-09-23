package test.java.bankmanager;


import main.java.bankmanager.dao.AccountDAO;
import main.java.bankmanager.model.CheckingAccount;

import java.util.List;

public class BankManagerDatabaseTest {
    public static void main(String[] args) throws Exception {
        AccountDAO accountDataAccess = new AccountDAO();

        accountDataAccess.insert(new CheckingAccount(1001, "Gika", 1500.00));
        accountDataAccess.insert(new CheckingAccount(1002, "Guinho", 1000.00));

        print(accountDataAccess.findAll());

        accountDataAccess.updateBalance(1001, 2000.00);
        accountDataAccess.transfer(1001, 1002, 250.00);

        print(accountDataAccess.findAll());

        accountDataAccess.remove(1001);
        accountDataAccess.remove(1002);
        System.out.println("Done.");
    }

    private static void print(List<CheckingAccount> accounts) {
        for (CheckingAccount checkingAccount : accounts) {
            System.out.printf("%d %s %.2f%n",
                    checkingAccount.getAccountNumber(),
                    checkingAccount.getAccountHolder(),
                    checkingAccount.getBalance());
        }
    }
}
