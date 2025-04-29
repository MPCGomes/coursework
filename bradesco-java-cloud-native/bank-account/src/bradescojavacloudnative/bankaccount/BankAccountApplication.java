package bradescojavacloudnative.bankaccount;

public class BankAccountApplication {
    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount();

        System.out.println("Welcome to Mary Bank! Please, follow the following instructions to create your new account.");

        System.out.println("Type your account number:");
        bankAccount.setAccountNumber(InputScanner.scanner.nextInt());

        System.out.println("Type your agency number:");
        bankAccount.setAgencyNumber(InputScanner.scanner.next());

        System.out.println("Type your name:");
        bankAccount.setClientName(InputScanner.scanner.next());

        System.out.println("Type your initial deposit:");
        bankAccount.setBalance(InputScanner.scanner.nextDouble());

        System.out.printf(
                """
                Greetings, %s, thank you for creating an account with us!" \s
                Your agency number is %s, your account number is %s, and your balance is %f and ready for withdraw!
                """,
                bankAccount.getClientName(),
                bankAccount.getAgencyNumber(),
                bankAccount.getAccountNumber(),
                bankAccount.getBalance()
        );
    }
}