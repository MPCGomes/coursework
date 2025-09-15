package bankmanager.service;

import bankmanager.model.CheckingAccount;

public final class FeeService {

    private FeeService() {
    }

    public static double computeFee(CheckingAccount checkingAccount, FeeStrategy feeStrategy) {
        return Math.max(0.0, feeStrategy.apply(checkingAccount.getBalance()));
    }

    public static double applyFee(CheckingAccount checkingAccount, FeeStrategy feeStrategy) {
        double fee = computeFee(checkingAccount, feeStrategy);
        if (fee <= 0.0) return 0.0;
        double newBalance = Math.max(0.0, checkingAccount.getBalance() - fee);
        checkingAccount.setBalance(newBalance);
        return fee;
    }
}
