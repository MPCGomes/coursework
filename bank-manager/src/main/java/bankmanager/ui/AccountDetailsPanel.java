package main.java.bankmanager.ui;

import main.java.bankmanager.model.CheckingAccount;

import javax.swing.*;
import java.awt.*;

public class AccountDetailsPanel extends JPanel {
    private final JTextField numberField = new JTextField(10);
    private final JTextField holderField = new JTextField(20);
    private final JTextField balanceField = new JTextField(10);
    private final JTextField amountField = new JTextField(8);

    public AccountDetailsPanel() {
        super(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Selected Account"));

        numberField.setEditable(false);
        holderField.setEditable(false);
        balanceField.setEditable(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.anchor = GridBagConstraints.LINE_END;

        constraints.gridx = 0;
        constraints.gridy = 0;
        add(new JLabel("Number:"), constraints);
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        add(numberField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Holder:"), constraints);
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        add(holderField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Balance:"), constraints);
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        add(balanceField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Amount:"), constraints);
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        add(amountField, constraints);
    }

    public void showAccount(CheckingAccount checkingAccount) {
        if (checkingAccount == null) {
            numberField.setText("");
            holderField.setText("");
            balanceField.setText("");
            return;
        }
        numberField.setText(String.valueOf(checkingAccount.getAccountNumber()));
        holderField.setText(checkingAccount.getAccountHolder());
        balanceField.setText(String.format("%.2f", checkingAccount.getBalance()));
    }

    public double readPositiveAmountOrMinusOne() {
        try {
            double value = Double.parseDouble(amountField.getText().trim());
            if (value <= 0) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Enter a positive numeric amount.",
                    "Invalid amount", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
    }

    public void updateBalance(double newBalance) {
        balanceField.setText(String.format("%.2f", newBalance));
    }
}
