import java.util.Scanner;

public class ATM {
    private BankAccount account;

    public ATM(BankAccount account) {
        this.account = account;
    }

    public void displayMenu() {
        System.out.println("======== ATM MENU ========");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Exit");
        System.out.println("==========================");
    }

    public void checkBalance() {
        System.out.printf("Current Balance: $%.2f%n", account.getBalance());
    }

    public void deposit(double amount) {
        if (amount > 0) {
            account.deposit(amount);
            System.out.printf("Successfully deposited $%.2f. New Balance: $%.2f%n", amount, account.getBalance());
        } else {
            System.out.println("Invalid deposit amount. Amount must be greater than zero.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0) {
            if (account.withdraw(amount)) {
                System.out.printf("Successfully withdrew $%.2f. New Balance: $%.2f%n", amount, account.getBalance());
            } else {
                System.out.println("Transaction failed: Insufficient funds.");
            }
        } else {
            System.out.println("Invalid withdrawal amount. Amount must be greater than zero.");
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            System.out.print("Choose an option (1-4): ");
            
            int choice = 0;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // clear invalid input
                System.out.println();
                continue;
            }

            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: $");
                    if (scanner.hasNextDouble()) {
                        double depositAmount = scanner.nextDouble();
                        deposit(depositAmount);
                    } else {
                        System.out.println("Invalid amount. Please enter a valid number.");
                        scanner.next(); // clear invalid input
                    }
                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: $");
                    if (scanner.hasNextDouble()) {
                        double withdrawAmount = scanner.nextDouble();
                        withdraw(withdrawAmount);
                    } else {
                        System.out.println("Invalid amount. Please enter a valid number.");
                        scanner.next(); // clear invalid input
                    }
                    break;
                case 4:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose a valid option from the menu.");
            }
            System.out.println(); // Add an empty line for readability
        }
        scanner.close();
    }

    public static void main(String[] args) {
        // Initialize an account with a default balance of $1000.00
        System.out.println("Initializing ATM system...");
        BankAccount userAccount = new BankAccount(1000.00);
        ATM atm = new ATM(userAccount);
        atm.start();
    }
}
