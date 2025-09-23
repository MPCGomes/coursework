package main.java.bankmanager.model;

import main.java.bankmanager.exception.InsufficientBalanceException;

public class CheckingAccount extends Account {
    public CheckingAccount(int accountNumber, String accountHolder, double balance) {
        super(accountNumber, accountHolder, balance);
    }

    @Override
    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal must be positive.");
        if (amount > getBalance()) throw new InsufficientBalanceException("Insufficient balance. Withdrawal denied.");
        setBalance(getBalance() - amount);
    }
}
