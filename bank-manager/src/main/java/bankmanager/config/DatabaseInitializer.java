package main.java.bankmanager.config;

import main.java.bankmanager.dao.AccountDAO;
import main.java.bankmanager.io.AccountFileManager;
import main.java.bankmanager.model.CheckingAccount;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public final class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initialize() throws IOException, SQLException {
        seedFromTextIfPresent("accounts.txt");
    }

    private static void seedFromTextIfPresent(String path) throws IOException, SQLException {
        File file = new File(path);
        if (!file.exists() || file.length() == 0L) return;

        List<CheckingAccount> accounts = AccountFileManager.readAccounts(path);
        if (accounts.isEmpty()) return;

        AccountDAO accountDAO = new AccountDAO();
        accountDAO.upsertAll(accounts);
        System.out.println("Seeded " + accounts.size() + " accounts from " + path + ".");
    }
}
