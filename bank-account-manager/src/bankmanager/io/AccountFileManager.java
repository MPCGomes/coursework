package bankmanager.io;

import bankmanager.model.CheckingAccount;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AccountFileManager {

    public static List<CheckingAccount> readAccounts(String filename) throws IOException {
        List<CheckingAccount> checkingAccountList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = line.split(",");
                if (fields.length < 3) continue;
                int accountNumber = Integer.parseInt(fields[0].trim());
                String accountHolder = fields[1].trim();
                double balance = Double.parseDouble(fields[2].trim());
                checkingAccountList.add(new CheckingAccount(accountNumber, accountHolder, balance));
            }
        }
        return checkingAccountList;
    }

    public static void saveAccounts(String filename, List<CheckingAccount> checkingAccountList) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename))) {
            for (CheckingAccount checkingAccount : checkingAccountList) {
                bufferedWriter.write(
                        checkingAccount.getAccountNumber() + "," +
                                checkingAccount.getAccountHolder() + "," +
                                String.format("%.2f", checkingAccount.getBalance())
                );
                bufferedWriter.newLine();
            }
        }
    }
}
