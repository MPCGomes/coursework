package bankmanager.ui;

import bankmanager.dao.AccountDAO;
import bankmanager.exception.InsufficientBalanceException;
import bankmanager.io.AccountFileManager;
import bankmanager.model.CheckingAccount;
import bankmanager.service.FeeService;
import bankmanager.service.FeeStrategy;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankManagerApp extends JFrame {

    private final AccountDAO accountDAO = new AccountDAO();
    private final AccountTableModel accountTableModel;

    private final JTable jTableAccounts = new JTable();
    private final JTextField jTextFieldNumber = new JTextField(10);
    private final JTextField jTextFieldHolder = new JTextField(20);
    private final JTextField jTextFieldBalance = new JTextField(10);
    private final JTextField jTextFieldAmount = new JTextField(8);

    private final JButton jButtonDeposit = new JButton("Deposit");
    private final JButton jButtonWithdraw = new JButton("Withdraw");
    private final JButton jButtonAddNew = new JButton("Add New");
    private final JButton jButtonDelete = new JButton("Delete");
    private final JButton jButtonTransfer = new JButton("Transfer");
    private final JButton jButtonExportToFile = new JButton("Export to File");

    private final JComboBox<FeeStrategy> jComboBoxFeeStrategy = new JComboBox<>(FeeStrategy.values());
    private final JButton jButtonComputeFee = new JButton("Compute Fee");
    private final JButton jButtonApplyFee = new JButton("Apply Fee");

    public BankManagerApp() {
        super("Bank Accounts Manager (DB)");
        List<CheckingAccount> checkingAccountList = loadAllFromDatabase();
        this.accountTableModel = new AccountTableModel(checkingAccountList);
        configureFrame();
        buildLayout();
        wireEvents();
    }

    private List<CheckingAccount> loadAllFromDatabase() {
        try {
            return accountDAO.findAll();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "DB read error: " + exception.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
    }

    private void buildLayout() {
        jTableAccounts.setModel(accountTableModel);
        jTableAccounts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane jScrollPaneAccounts = new JScrollPane(jTableAccounts);
        add(jScrollPaneAccounts, BorderLayout.CENTER);

        JPanel jPanelDetails = new JPanel(new GridBagLayout());
        jPanelDetails.setBorder(BorderFactory.createTitledBorder("Selected Account"));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(4, 4, 4, 4);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        jPanelDetails.add(new JLabel("Number:"), gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        jTextFieldNumber.setEditable(false);
        jPanelDetails.add(jTextFieldNumber, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        jPanelDetails.add(new JLabel("Holder:"), gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        jTextFieldHolder.setEditable(false);
        jPanelDetails.add(jTextFieldHolder, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        jPanelDetails.add(new JLabel("Balance:"), gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        jTextFieldBalance.setEditable(false);
        jPanelDetails.add(jTextFieldBalance, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        jPanelDetails.add(new JLabel("Amount:"), gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        jPanelDetails.add(jTextFieldAmount, gridBagConstraints);

        JPanel jPanelActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        jPanelActions.add(jButtonDeposit);
        jPanelActions.add(jButtonWithdraw);
        jPanelActions.add(jButtonAddNew);
        jPanelActions.add(jButtonDelete);
        jPanelActions.add(jButtonTransfer);

        jPanelActions.add(new JLabel("Fee strategy:"));
        jComboBoxFeeStrategy.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.label()));
        jPanelActions.add(jComboBoxFeeStrategy);
        jPanelActions.add(jButtonComputeFee);
        jPanelActions.add(jButtonApplyFee);

        jPanelActions.add(jButtonExportToFile);

        JPanel jPanelSouth = new JPanel(new BorderLayout());
        jPanelSouth.add(jPanelDetails, BorderLayout.CENTER);
        jPanelSouth.add(jPanelActions, BorderLayout.SOUTH);
        add(jPanelSouth, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void wireEvents() {
        jTableAccounts.getSelectionModel().addListSelectionListener(this::onRowSelected);
        jButtonDeposit.addActionListener(e -> onDeposit());
        jButtonWithdraw.addActionListener(e -> onWithdraw());
        jButtonAddNew.addActionListener(e -> onAddNew());
        jButtonDelete.addActionListener(e -> onDelete());
        jButtonTransfer.addActionListener(e -> onTransfer());
        jButtonComputeFee.addActionListener(e -> onComputeFee());
        jButtonApplyFee.addActionListener(e -> onApplyFee());
        jButtonExportToFile.addActionListener(e -> onExportToFile());
    }

    private void onRowSelected(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getValueIsAdjusting()) return;
        int selectedRow = jTableAccounts.getSelectedRow();
        if (selectedRow < 0) {
            jTextFieldNumber.setText("");
            jTextFieldHolder.setText("");
            jTextFieldBalance.setText("");
            return;
        }
        CheckingAccount checkingAccount = accountTableModel.getAt(selectedRow);
        jTextFieldNumber.setText(String.valueOf(checkingAccount.getAccountNumber()));
        jTextFieldHolder.setText(checkingAccount.getAccountHolder());
        jTextFieldBalance.setText(String.format("%.2f", checkingAccount.getBalance()));
    }

    private CheckingAccount selectedOrWarn() {
        int selectedRow = jTableAccounts.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select an account first.");
            return null;
        }
        return accountTableModel.getAt(selectedRow);
    }

    private double readPositiveAmount() {
        try {
            double value = Double.parseDouble(jTextFieldAmount.getText().trim());
            if (value <= 0) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Enter a positive numeric amount.",
                    "Invalid amount", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
    }

    private void onDeposit() {
        CheckingAccount checkingAccount = selectedOrWarn();
        if (checkingAccount == null) return;
        double value = readPositiveAmount();
        if (value <= 0) return;

        try {
            checkingAccount.deposit(value);
            accountDAO.updateBalance(checkingAccount.getAccountNumber(), checkingAccount.getBalance());
            int row = jTableAccounts.getSelectedRow();
            accountTableModel.refreshRow(row);
            jTextFieldBalance.setText(String.format("%.2f", checkingAccount.getBalance()));
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
        double value = readPositiveAmount();
        if (value <= 0) return;

        try {
            checkingAccount.withdraw(value);
            accountDAO.updateBalance(checkingAccount.getAccountNumber(), checkingAccount.getBalance());
            int row = jTableAccounts.getSelectedRow();
            accountTableModel.refreshRow(row);
            jTextFieldBalance.setText(String.format("%.2f", checkingAccount.getBalance()));
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
        JTextField jTextFieldNewNumber = new JTextField();
        JTextField jTextFieldNewHolder = new JTextField();
        JTextField jTextFieldInitialBalance = new JTextField();

        JPanel jPanelForm = new JPanel(new GridLayout(0, 1));
        jPanelForm.add(new JLabel("Number:"));
        jPanelForm.add(jTextFieldNewNumber);
        jPanelForm.add(new JLabel("Holder:"));
        jPanelForm.add(jTextFieldNewHolder);
        jPanelForm.add(new JLabel("Initial Balance:"));
        jPanelForm.add(jTextFieldInitialBalance);

        if (JOptionPane.showConfirmDialog(this, jPanelForm, "Add New Account",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

        try {
            int number = Integer.parseInt(jTextFieldNewNumber.getText().trim());
            String holder = jTextFieldNewHolder.getText().trim();
            double initialBalance = Double.parseDouble(jTextFieldInitialBalance.getText().trim());
            if (holder.isEmpty()) throw new IllegalArgumentException("Holder cannot be empty.");

            CheckingAccount newCheckingAccount = new CheckingAccount(number, holder, initialBalance);
            accountDAO.insert(newCheckingAccount);
            accountTableModel.add(newCheckingAccount);

            int newRow = accountTableModel.getRowCount() - 1;
            jTableAccounts.setRowSelectionInterval(newRow, newRow);
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
            accountDAO.remove(checkingAccount.getAccountNumber());
            int row = jTableAccounts.getSelectedRow();
            accountTableModel.removeAt(row);
            jTextFieldNumber.setText("");
            jTextFieldHolder.setText("");
            jTextFieldBalance.setText("");
            JOptionPane.showMessageDialog(this, "Account deleted.");
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "DB error: " + exception.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onTransfer() {
        CheckingAccount fromCheckingAccount = selectedOrWarn();
        if (fromCheckingAccount == null) return;

        JTextField jTextFieldDestination = new JTextField();
        JTextField jTextFieldTransferAmount = new JTextField();

        JPanel jPanelTransfer = new JPanel(new GridLayout(0, 1));
        jPanelTransfer.add(new JLabel("Destination account number:"));
        jPanelTransfer.add(jTextFieldDestination);
        jPanelTransfer.add(new JLabel("Amount:"));
        jPanelTransfer.add(jTextFieldTransferAmount);

        if (JOptionPane.showConfirmDialog(this, jPanelTransfer, "Transfer",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

        try {
            int toNumber = Integer.parseInt(jTextFieldDestination.getText().trim());
            double amount = Double.parseDouble(jTextFieldTransferAmount.getText().trim());
            if (amount <= 0) throw new NumberFormatException();

            accountDAO.transfer(fromCheckingAccount.getAccountNumber(), toNumber, amount);

            List<CheckingAccount> fresh = accountDAO.findAll();
            accountTableModel.all().clear();
            accountTableModel.all().addAll(fresh);
            accountTableModel.refreshAll();

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
        FeeStrategy feeStrategy = (FeeStrategy) jComboBoxFeeStrategy.getSelectedItem();
        double fee = FeeService.computeFee(checkingAccount, feeStrategy);
        JOptionPane.showMessageDialog(this, String.format("%s fee: R$ %.2f", feeStrategy.label(), fee));
    }

    private void onApplyFee() {
        CheckingAccount checkingAccount = selectedOrWarn();
        if (checkingAccount == null) return;
        FeeStrategy feeStrategy = (FeeStrategy) jComboBoxFeeStrategy.getSelectedItem();
        double charged = FeeService.applyFee(checkingAccount, feeStrategy);
        int row = jTableAccounts.getSelectedRow();
        accountTableModel.refreshRow(row);
        jTextFieldBalance.setText(String.format("%.2f", checkingAccount.getBalance()));
        JOptionPane.showMessageDialog(this, String.format("Applied %s: R$ %.2f", feeStrategy.label(), charged));
        try {
            accountDAO.updateBalance(checkingAccount.getAccountNumber(), checkingAccount.getBalance());
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "DB error: " + exception.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onExportToFile() {
        try {
            AccountFileManager.saveAccounts("updated_accounts.txt", accountTableModel.all());
            JOptionPane.showMessageDialog(this, "Exported to updated_accounts.txt.");
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(this, "Export error: " + exception.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
