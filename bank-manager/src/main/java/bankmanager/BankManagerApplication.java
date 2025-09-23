package main.java.bankmanager;

import main.java.bankmanager.config.DatabaseInitializer;
import main.java.bankmanager.ui.BankManagerApp;

import javax.swing.*;

public class BankManagerApplication {
    public static void main(String[] args) {
        try {
            DatabaseInitializer.initialize();
        } catch (Exception exception) {
            System.err.println("Startup error: " + exception.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            BankManagerApp bankManagerApp = new BankManagerApp();
            bankManagerApp.setVisible(true);
        });
    }
}
