package main.java.bankmanager.io;

import main.java.bankmanager.model.CheckingAccount;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AccountFileManager {

    private AccountFileManager() {
    }

    public static List<CheckingAccount> readAccounts(String filename) throws IOException {
        List<CheckingAccount> accounts = new ArrayList<>();
        try (BufferedReader bufferedReader =
                     new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                String[] fields = trimmed.split(",");
                if (fields.length < 3) continue;

                int number = Integer.parseInt(fields[0].trim());
                String holder = fields[1].trim();
                double balance = Double.parseDouble(fields[2].trim());

                accounts.add(new CheckingAccount(number, holder, balance));
            }
        }
        return accounts;
    }

    public static void saveAccounts(String filename, List<CheckingAccount> accounts) throws IOException {
        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(new FileWriter(filename, StandardCharsets.UTF_8))) {
            for (CheckingAccount account : accounts) {
                bufferedWriter.write(account.getAccountNumber() + "," +
                        account.getAccountHolder() + "," +
                        String.format("%.2f", account.getBalance()));
                bufferedWriter.newLine();
            }
        }
    }
}