package main.java.bankmanager.ui;

import main.java.bankmanager.dao.AccountDAO;
import main.java.bankmanager.exception.InsufficientBalanceException;
import main.java.bankmanager.io.AccountFileManager;
import main.java.bankmanager.model.CheckingAccount;
import main.java.bankmanager.service.FeeService;
import main.java.bankmanager.service.FeeStrategy;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankManagerApp extends JFrame {

    private final AccountDAO accountDataAccess = new AccountDAO();

    private final AccountsTablePanel accountsTablePanel;
    private final AccountDetailsPanel accountDetailsPanel = new AccountDetailsPanel();
    private final ActionsPanel actionsPanel = new ActionsPanel();

    public BankManagerApp() {
        super("Bank Accounts Manager (DB)");
        List<CheckingAccount> accounts = loadAllFromDatabase();
        this.accountsTablePanel = new AccountsTablePanel(accounts);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        add(accountsTablePanel, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.add(accountDetailsPanel, BorderLayout.CENTER);
        south.add(actionsPanel, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        wireEvents();
        pack();
        setLocationRelativeTo(null);
    }

    private List<CheckingAccount> loadAllFromDatabase() {
        try {
            return accountDataAccess.findAll();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "DB read error: " + exception.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    private void wireEvents() {
        accountsTablePanel.getAccountsTable()
                .getSelectionModel()
                .addListSelectionListener(this::onRowSelected);

        actionsPanel.getDepositButton().addActionListener(e -> onDeposit());
        actionsPanel.getWithdrawButton().addActionListener(e -> onWithdraw());
        actionsPanel.getAddNewButton().addActionListener(e -> onAddNew());
        actionsPanel.getDeleteButton().addActionListener(e -> onDelete());
        actionsPanel.getTransferButton().addActionListener(e -> onTransfer());
        actionsPanel.getComputeFeeButton().addActionListener(e -> onComputeFee());
        actionsPanel.getApplyFeeButton().addActionListener(e -> onApplyFee());
        actionsPanel.getExportToFileButton().addActionListener(e -> onExportToFile());
    }

    private void onRowSelected(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) return;
        accountDetailsPanel.showAccount(accountsTablePanel.getSelectedAccount());
    }

    private CheckingAccount selectedOrWarn() {
        CheckingAccount checkingAccount = accountsTablePanel.getSelectedAccount();
        if (checkingAccount == null) {
            JOptionPane.showMessageDialog(this, "Select an account first.");
        }
        return checkingAccount;
    }

    private void onDeposit() {
        CheckingAccount checkingAccount = selectedOrWarn();
        if (checkingAccount == null) return;
        double value = accountDetailsPanel.readPositiveAmountOrMinusOne();
        if (value <= 0) return;

        try {
            checkingAccount.deposit(value);
            accountDataAccess.updateBalance(checkingAccount.getAccountNumber(), checkingAccount.getBalance());
            accountsTablePanel.getAccountTableModel().refreshRow(
                    accountsTablePanel.getAccountsTable().getSelectedRow());
            accountDetailsPanel.updateBalance(checkingAccount.getBalance());
            JOptionPane.showMessageDialog(this, "Deposit successful.");
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "DB error: " + exception.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Invalid", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onWithdraw() {
        CheckingAccount checkingAccount = selectedOrWarn();
        if (checkingAccount == null) return;
        double value = accountDetailsPanel.readPositiveAmountOrMinusOne();
        if (value <= 0) return;

        try {
            checkingAccount.withdraw(value);
            accountDataAccess.updateBalance(checkingAccount.getAccountNumber(), checkingAccount.getBalance());
            accountsTablePanel.getAccountTableModel().refreshRow(
                    accountsTablePanel.getAccountsTable().getSelectedRow());
            accountDetailsPanel.updateBalance(checkingAccount.getBalance());
            JOptionPane.showMessageDialog(this, "Withdrawal successful.");
        } catch (InsufficientBalanceException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Insufficient Balance", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "DB error: " + exception.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Invalid", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAddNew() {
        JTextField numberField = new JTextField();
        JTextField holderField = new JTextField();
        JTextField initialBalanceField = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(0, 1));
        formPanel.add(new JLabel("Number:"));
        formPanel.add(numberField);
        formPanel.add(new JLabel("Holder:"));
        formPanel.add(holderField);
        formPanel.add(new JLabel("Initial Balance:"));
        formPanel.add(initialBalanceField);

        if (JOptionPane.showConfirmDialog(this, formPanel, "Add New Account",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

        try {
            int number = Integer.parseInt(numberField.getText().trim());
            String holder = holderField.getText().trim();
            double initialBalance = Double.parseDouble(initialBalanceField.getText().trim());
            if (holder.isEmpty()) throw new IllegalArgumentException("Holder cannot be empty.");

            CheckingAccount newAccount = new CheckingAccount(number, holder, initialBalance);
            accountDataAccess.insert(newAccount);
            accountsTablePanel.getAccountTableModel().add(newAccount);

            int newRow = accountsTablePanel.getAccountTableModel().getRowCount() - 1;
            accountsTablePanel.getAccountsTable().setRowSelectionInterval(newRow, newRow);
            JOptionPane.showMessageDialog(this, "Account created.");
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Invalid number or balance.", "Invalid", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "DB error: " + exception.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete() {
        CheckingAccount checkingAccount = selectedOrWarn();
        if (checkingAccount == null) return;
        if (JOptionPane.showConfirmDialog(this,
                "Delete account #" + checkingAccount.getAccountNumber() + "?",
                "Confirm", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

        try {
            accountDataAccess.remove(checkingAccount.getAccountNumber());
            int row = accountsTablePanel.getAccountsTable().getSelectedRow();
            accountsTablePanel.getAccountTableModel().removeAt(row);
            accountDetailsPanel.showAccount(null);
            JOptionPane.showMessageDialog(this, "Account deleted.");
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "DB error: " + exception.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onTransfer() {
        CheckingAccount originAccount = selectedOrWarn();
        if (originAccount == null) return;

        JTextField destinationField = new JTextField();
        JTextField transferAmountField = new JTextField();

        JPanel transferPanel = new JPanel(new GridLayout(0, 1));
        transferPanel.add(new JLabel("Destination account number:"));
        transferPanel.add(destinationField);
        transferPanel.add(new JLabel("Amount:"));
        transferPanel.add(transferAmountField);

        if (JOptionPane.showConfirmDialog(this, transferPanel, "Transfer",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

        try {
            int destinationNumber = Integer.parseInt(destinationField.getText().trim());
            double amount = Double.parseDouble(transferAmountField.getText().trim());
            if (amount <= 0) throw new NumberFormatException();

            accountDataAccess.transfer(originAccount.getAccountNumber(), destinationNumber, amount);

            List<CheckingAccount> fresh = accountDataAccess.findAll();
            AccountTableModel model = accountsTablePanel.getAccountTableModel();
            model.all().clear();
            model.all().addAll(fresh);
            model.refreshAll();

            JOptionPane.showMessageDialog(this, "Transfer successful.");
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Enter a valid destination number and positive amount.",
                    "Invalid", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "DB error: " + exception.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onComputeFee() {
        CheckingAccount checkingAccount = selectedOrWarn();
        if (checkingAccount == null) return;
        FeeStrategy feeStrategy = (FeeStrategy) actionsPanel.getFeeStrategyComboBox().getSelectedItem();
        double fee = FeeService.computeFee(checkingAccount, feeStrategy);
        JOptionPane.showMessageDialog(this, String.format("%s fee: R$ %.2f", feeStrategy.label(), fee));
    }

    private void onApplyFee() {
        CheckingAccount checkingAccount = selectedOrWarn();
        if (checkingAccount == null) return;
        FeeStrategy feeStrategy = (FeeStrategy) actionsPanel.getFeeStrategyComboBox().getSelectedItem();
        double charged = FeeService.applyFee(checkingAccount, feeStrategy);
        int row = accountsTablePanel.getAccountsTable().getSelectedRow();
        accountsTablePanel.getAccountTableModel().refreshRow(row);
        accountDetailsPanel.updateBalance(checkingAccount.getBalance());
        JOptionPane.showMessageDialog(this, String.format("Applied %s: R$ %.2f", feeStrategy.label(), charged));
        try {
            accountDataAccess.updateBalance(checkingAccount.getAccountNumber(), checkingAccount.getBalance());
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "DB error: " + exception.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onExportToFile() {
        try {
            AccountTableModel model = accountsTablePanel.getAccountTableModel();
            AccountFileManager.saveAccounts("updated_accounts.txt", model.all());
            JOptionPane.showMessageDialog(this, "Exported to updated_accounts.txt.");
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(this, "Export error: " + exception.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}