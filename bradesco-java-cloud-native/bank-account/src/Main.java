public class Main {
    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount();

        System.out.println("Welcome to Mary Bank! Please, follow the following instructions to create your new account.");

        System.out.println("Type your account number:");
        bankAccount.setAccountNumber(InputUtil.scanner.nextInt());

        System.out.println("Type your agency number:");
        bankAccount.setAgencyNumber(InputUtil.scanner.next());

        System.out.println("Type your name:");
        bankAccount.setClientName(InputUtil.scanner.next());

        System.out.println("Type your initial balance:");
        bankAccount.setBalance(InputUtil.scanner.nextDouble());

        System.out.printf(
                "Greetings, %s, thank you for creating an account with us!" +
                "Your agency number is %s, your account number is %s, and your balance is %f and ready for withdraw!",
                bankAccount.getClientName(),
                bankAccount.getAgencyNumber(),
                bankAccount.getAccountNumber(),
                bankAccount.getBalance()
        );
    }
}