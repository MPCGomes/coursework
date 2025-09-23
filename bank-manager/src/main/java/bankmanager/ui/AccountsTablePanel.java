package main.java.bankmanager.ui;

import main.java.bankmanager.model.CheckingAccount;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AccountsTablePanel extends JPanel {
    private final JTable accountsTable = new JTable();
    private final AccountTableModel accountTableModel;

    public AccountsTablePanel(List<CheckingAccount> accounts) {
        super(new BorderLayout());
        this.accountTableModel = new AccountTableModel(accounts);
        accountsTable.setModel(accountTableModel);
        accountsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(accountsTable), BorderLayout.CENTER);
    }

    public JTable getAccountsTable() { return accountsTable; }

    public AccountTableModel getAccountTableModel() { return accountTableModel; }

    public CheckingAccount getSelectedAccount() {
        int row = accountsTable.getSelectedRow();
        if (row < 0) return null;
        return accountTableModel.getAt(row);
    }
}
