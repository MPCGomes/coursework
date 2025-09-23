package main.java.bankmanager.service;

import main.java.bankmanager.model.CheckingAccount;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class AccountService {

    public static final Predicate<CheckingAccount> BALANCE_GREATER_THAN_5000 =
            checkingAccount -> checkingAccount.getBalance() > 5000.0;
    public static final Predicate<CheckingAccount> EVEN_ACCOUNT_NUMBER =
            checkingAccount -> checkingAccount.getAccountNumber() % 2 == 0;

    private AccountService() {
    }

    public static List<CheckingAccount> filterBalanceGreaterThan(List<CheckingAccount> checkingAccountList, double minimum) {
        return checkingAccountList.stream()
                .filter(checkingAccount -> checkingAccount.getBalance() > minimum)
                .collect(Collectors.toList());
    }

    public static double totalBalance(List<CheckingAccount> checkingAccountList) {
        return checkingAccountList.stream()
                .map(CheckingAccount::getBalance)
                .reduce(0.0, Double::sum);
    }

    public static BalanceRange classify(CheckingAccount checkingAccount) {
        double balance = checkingAccount.getBalance();
        if (balance <= 5000.0) return BalanceRange.UP_TO_5000;
        if (balance <= 10000.0) return BalanceRange.FROM_5001_TO_10000;
        return BalanceRange.ABOVE_10000;
    }

    public static Map<BalanceRange, List<CheckingAccount>> groupByRange(List<CheckingAccount> checkingAccountList) {
        return checkingAccountList.stream()
                .collect(Collectors.groupingBy(AccountService::classify, LinkedHashMap::new, Collectors.toList()));
    }

    public static List<CheckingAccount> filter(List<CheckingAccount> checkingAccountList,
                                               Predicate<CheckingAccount> predicate) {
        return checkingAccountList.stream().filter(predicate).collect(Collectors.toList());
    }

    public static void sortByBalanceDesc(List<CheckingAccount> checkingAccountList) {
        checkingAccountList.sort(Comparator.comparingDouble(CheckingAccount::getBalance).reversed());
    }

    public static void sortByHolderAsc(List<CheckingAccount> checkingAccountList) {
        checkingAccountList.sort(Comparator.comparing(CheckingAccount::getAccountHolder, String.CASE_INSENSITIVE_ORDER));
    }

    public static List<CheckingAccount> sortedCopy(List<CheckingAccount> checkingAccountList,
                                                   Comparator<CheckingAccount> comparator) {
        List<CheckingAccount> checkingAccountListCopy = new ArrayList<>(checkingAccountList);
        checkingAccountListCopy.sort(comparator);
        return checkingAccountListCopy;
    }

    public enum BalanceRange {UP_TO_5000, FROM_5001_TO_10000, ABOVE_10000}
}
