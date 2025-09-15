package bankmanager.dao;

import bankmanager.config.DatabaseConfig;
import bankmanager.model.CheckingAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public void insert(CheckingAccount checkingAccount) throws SQLException {
        String sql = "INSERT INTO accounts (number, holder, balance) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, checkingAccount.getAccountNumber());
            preparedStatement.setString(2, checkingAccount.getAccountHolder());
            preparedStatement.setDouble(3, checkingAccount.getBalance());
            preparedStatement.executeUpdate();
        }
    }

    public List<CheckingAccount> findAll() throws SQLException {
        String sql = "SELECT number, holder, balance FROM accounts ORDER BY number";
        List<CheckingAccount> checkingAccountList = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                checkingAccountList.add(new CheckingAccount(
                        resultSet.getInt("number"),
                        resultSet.getString("holder"),
                        resultSet.getDouble("balance")));
            }
        }
        return checkingAccountList;
    }

    public CheckingAccount findByNumber(int number) throws SQLException {
        String sql = "SELECT number, holder, balance FROM accounts WHERE number = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, number);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new CheckingAccount(
                            resultSet.getInt("number"),
                            resultSet.getString("holder"),
                            resultSet.getDouble("balance"));
                }
            }
        }
        return null;
    }

    public void updateBalance(int number, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE number = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setInt(2, number);
            preparedStatement.executeUpdate();
        }
    }

    public void remove(int number) throws SQLException {
        String sql = "DELETE FROM accounts WHERE number = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, number);
            preparedStatement.executeUpdate();
        }
    }

    // Atomic transfer transaction
    public void transfer(int fromNumber, int toNumber, double amount) throws SQLException {
        if (amount <= 0) throw new SQLException("Amount must be positive.");

        String selectForUpdate = "SELECT number, balance FROM accounts WHERE number IN (?, ?) FOR UPDATE";
        String withdrawSql = "UPDATE accounts SET balance = balance - ? WHERE number = ?";
        String depositSql = "UPDATE accounts SET balance = balance + ? WHERE number = ?";

        try (Connection connection = DatabaseConfig.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement lockStatement = connection.prepareStatement(selectForUpdate)) {
                lockStatement.setInt(1, fromNumber);
                lockStatement.setInt(2, toNumber);
                double fromBalance = -1;
                boolean fromFound = false, toFound = false;

                try (ResultSet resultSet = lockStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int accountNumber = resultSet.getInt("number");
                        double accountBalance = resultSet.getDouble("balance");
                        if (accountNumber == fromNumber) {
                            fromFound = true;
                            fromBalance = accountBalance;
                        }
                        if (accountNumber == toNumber) {
                            toFound = true;
                        }
                    }
                }

                if (!fromFound || !toFound) {
                    connection.rollback();
                    throw new SQLException("One or both accounts not found.");
                }
                if (fromBalance < amount) {
                    connection.rollback();
                    throw new SQLException("Insufficient balance for transfer.");
                }

                try (PreparedStatement withdrawStatement = connection.prepareStatement(withdrawSql);
                     PreparedStatement depositStatement = connection.prepareStatement(depositSql)) {

                    withdrawStatement.setDouble(1, amount);
                    withdrawStatement.setInt(2, fromNumber);
                    withdrawStatement.executeUpdate();

                    depositStatement.setDouble(1, amount);
                    depositStatement.setInt(2, toNumber);
                    depositStatement.executeUpdate();
                }

                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }
}
