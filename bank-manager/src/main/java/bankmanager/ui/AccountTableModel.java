package main.java.bankmanager.ui;

import main.java.bankmanager.model.CheckingAccount;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class AccountTableModel extends AbstractTableModel {
    private final List<CheckingAccount> checkingAccountList;
    private final String[] columns = {"Number", "Holder", "Balance"};

    public AccountTableModel(List<CheckingAccount> checkingAccountList) {
        this.checkingAccountList = checkingAccountList;
    }

    public List<CheckingAccount> all() {
        return checkingAccountList;
    }

    public CheckingAccount getAt(int row) {
        return checkingAccountList.get(row);
    }

    public void add(CheckingAccount checkingAccount) {
        checkingAccountList.add(checkingAccount);
        int row = checkingAccountList.size() - 1;
        fireTableRowsInserted(row, row);
    }

    public void removeAt(int row) {
        checkingAccountList.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void refreshRow(int row) {
        fireTableRowsUpdated(row, row);
    }

    public void refreshAll() {
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return checkingAccountList.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CheckingAccount checkingAccount = checkingAccountList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> checkingAccount.getAccountNumber();
            case 1 -> checkingAccount.getAccountHolder();
            case 2 -> String.format("%.2f", checkingAccount.getBalance());
            default -> "";
        };
    }
}
