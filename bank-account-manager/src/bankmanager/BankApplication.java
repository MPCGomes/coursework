package bankmanager;

import bankmanager.ui.BankManagerApp;

import javax.swing.*;

public class BankApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankManagerApp bankManagerApp = new BankManagerApp();
            bankManagerApp.setVisible(true);
        });
    }
}
