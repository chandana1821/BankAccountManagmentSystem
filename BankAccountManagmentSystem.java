import java.text.DecimalFormat;
import java.util.*;

class Customer {
    private final long accNo;
    private final String name;
    private final String password;
    private static long accCounter = 1000000;

    Customer(String name, String password) {
        this.name = name;
        this.password = password;
        this.accNo = ++accCounter;
    }

    public long getAccNo() {
        return accNo;
    }

    public String getName() {
        return name;
    }

    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}

abstract class Account {
    protected double balance;

    Account(double initialAmount) {
        this.balance = initialAmount;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("--SUCCESSFULLY DEPOSITED: $" + amount + "--");
        } else {
            System.out.println("--INVALID DEPOSIT AMOUNT--");
        }
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("--INVALID WITHDRAW AMOUNT--");
        } else if (this.balance < amount) {
            System.out.println("--INSUFFICIENT BALANCE--");
        } else {
            this.balance -= amount;
            System.out.println("--SUCCESSFULLY WITHDRAWN: $" + amount + "--");
        }
    }

    public void showBalance() {
        DecimalFormat df = new DecimalFormat("Rs #,##0.00");
        System.out.println("--YOUR CURRENT BALANCE: " + df.format(this.balance) + "--");
    }
}

class SavingsAccount extends Account {
    SavingsAccount(double initialAmount) {
        super(initialAmount);
    }
}

class CheckingAccount extends Account {
    CheckingAccount(double initialAmount) {
        super(initialAmount);
    }
}

class Bank {
    private final List<Customer> customers = new ArrayList<>();
    private final Map<Long, Account> accounts = new HashMap<>();
    private final Scanner sc = new Scanner(System.in);

    void createAccount() {
        System.out.print("Enter your name: ");
        String name = sc.next();
        System.out.print("Enter initial deposit: ");
        double initialAmount = sc.nextDouble();
        
        String password;
        while (true) {
            System.out.print("Create a password (at least 8 characters): ");
            password = sc.next();
            if (password.length() >= 8) {
                break;
            }
            System.out.println("--PASSWORD TOO SHORT, PLEASE RE-ENTER--");
        }
        
        System.out.print("Confirm password: ");
        String confirmPassword = sc.next();

        if (!password.equals(confirmPassword)) {
            System.out.println("--PASSWORDS DO NOT MATCH, PLEASE TRY AGAIN--");
            return;
        }

        Customer customer = new Customer(name, password);
        System.out.println("Select account type:\n1. Savings Account\n2. Checking Account");
        int choice = sc.nextInt();

        Account account = (choice == 1) ? new SavingsAccount(initialAmount) : new CheckingAccount(initialAmount);
        customers.add(customer);
        accounts.put(customer.getAccNo(), account);

        System.out.println("--ACCOUNT CREATED SUCCESSFULLY!--");
        System.out.println("Your Account Number: " + customer.getAccNo());
    }

    void login() {
        System.out.print("Enter Account Number: ");
        long accNo = sc.nextLong();
        
        Customer customer = findCustomer(accNo);
        if (customer == null) {
            System.out.println("--INVALID ACCOUNT NUMBER--");
            return;
        }
        
        Account account = accounts.get(accNo);
        
        while (true) {
            System.out.print("Enter Password: ");
            String enteredPassword = sc.next();
            
            if (customer.validatePassword(enteredPassword)) {
                System.out.println("--LOGIN SUCCESSFUL. WELCOME " + customer.getName() + "--");
                break;
            } else {
                System.out.println("--INCORRECT PASSWORD, PLEASE TRY AGAIN--");
            }
        }

        while (true) {
            System.out.println("Select Operation:\n1. Deposit\n2. Withdraw\n3. Balance Enquiry\n4. Logout");
            int op = sc.nextInt();

            switch (op) {
                case 1:
                    System.out.print("Enter amount to deposit: ");
                    account.deposit(sc.nextDouble());
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    account.withdraw(sc.nextDouble());
                    break;
                case 3:
                    account.showBalance();
                    break;
                case 4:
                    System.out.println("--LOGGING OUT--");
                    return;
                default:
                    System.out.println("--INVALID OPTION--");
                    break;
            }
        }
    }

    private Customer findCustomer(long accNo) {
        return customers.stream().filter(c -> c.getAccNo() == accNo).findFirst().orElse(null);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {
            System.out.println("*** BANK MANAGEMENT SYSTEM ***");
            System.out.println("1. Create New Account\n2. Login\n3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    bank.createAccount();
                    break;
                case 2:
                    bank.login();
                    break;
                case 3:
                    System.out.println("** Thank you for using our banking system **");
                    sc.close();
                    return;
                default:
                    System.out.println("--INVALID CHOICE--");
                    break;
            }
        }
    }
}
