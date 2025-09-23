package main.java.bankmanager.ui;

import main.java.bankmanager.service.FeeStrategy;

import javax.swing.*;
import java.awt.*;

public class ActionsPanel extends JPanel {
    private final JButton depositButton = new JButton("Deposit");
    private final JButton withdrawButton = new JButton("Withdraw");
    private final JButton addNewButton = new JButton("Add New");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton transferButton = new JButton("Transfer");
    private final JButton exportToFileButton = new JButton("Export to File");

    private final JComboBox<FeeStrategy> feeStrategyComboBox = new JComboBox<>(FeeStrategy.values());
    private final JButton computeFeeButton = new JButton("Compute Fee");
    private final JButton applyFeeButton = new JButton("Apply Fee");

    public ActionsPanel() {
        super(new FlowLayout(FlowLayout.RIGHT));
        add(depositButton);
        add(withdrawButton);
        add(addNewButton);
        add(deleteButton);
        add(transferButton);

        add(new JLabel("Fee strategy:"));
        feeStrategyComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.label()));
        add(feeStrategyComboBox);
        add(computeFeeButton);
        add(applyFeeButton);

        add(exportToFileButton);
    }

    public JButton getDepositButton() {
        return depositButton;
    }

    public JButton getWithdrawButton() {
        return withdrawButton;
    }

    public JButton getAddNewButton() {
        return addNewButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getTransferButton() {
        return transferButton;
    }

    public JButton getExportToFileButton() {
        return exportToFileButton;
    }

    public JComboBox<FeeStrategy> getFeeStrategyComboBox() {
        return feeStrategyComboBox;
    }

    public JButton getComputeFeeButton() {
        return computeFeeButton;
    }

    public JButton getApplyFeeButton() {
        return applyFeeButton;
    }
}

