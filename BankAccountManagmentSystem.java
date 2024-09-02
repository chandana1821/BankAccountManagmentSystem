import java.text.DecimalFormat;
import java.util.*;

class Customer {
    private Double accNo;
    private String name;
    private String password;
    private static double accCounter = 1000000;

    Customer(String name, String password) {
        this.name = name;
        this.password = password;
        this.accNo = ++accCounter;
    }

    public Double getAccNo() {
        return accNo;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}

class Account {
    protected Double balance;

    Account(double initialAmount) {
        this.balance = initialAmount;
    }

    public Double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
        System.out.println("--SUCCESSFULLY DEPOSITED AMOUNT: " + amount+"--");
    }

    public void withdraw(double amount) {
        if (this.balance <= 0) {
            System.out.println("--INVALID WITHDRAW--");
        } else if (this.balance < amount) {
            System.out.println("--INSUFFICIENT BALANCE--");
        } else {
            this.balance -= amount;
            System.out.println("--SUCCESSFULLY WITHDRAWN AMOUNT: " + amount+"--");
        }
    }

    public void showBalance() {
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("--YOUR CURRENT BALANCE IS: " + df.format(this.balance)+"--");
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
    private List<Customer> customers;
    private Map<Double, Account> accounts;
    private Scanner sc;

    Bank() {
        customers = new ArrayList<>();
        accounts = new HashMap<>();
        sc = new Scanner(System.in);
    }

    void createAccount() {
        System.out.println("ENTER YOUR NAME:");
        String name = sc.next();
        System.out.println("ENTER INITIAL AMOUNT:");
        double initialAmount = sc.nextDouble();
        System.out.println("ENTER YOUR PASSWORD:");
        String password = sc.next();
        System.out.println("CONFIRM PASSWORD:");
        String confirmPassword = sc.next();
        if (!password.equals(confirmPassword)) {
            System.out.println("--PASSWORD NOT MATCHING--");
            return;
        }

        Customer customer = new Customer(name, password);
        Account account;
        System.out.println("SELECT ACCOUNT TYPE:\n1. SAVING ACCOUNT\n2. CHECKING ACCOUNT");
        int choice = sc.nextInt();
        if (choice == 1) {
            account = new SavingsAccount(initialAmount);
        } else {
            account = new CheckingAccount(initialAmount);
        }
        customers.add(customer);
        accounts.put(customer.getAccNo(), account);
        System.out.println("--SUCCESSFULLY CREATED YOUR ACCOUNT!!!--");
        System.out.println("Your account number is " + customer.getAccNo());
    }

    public void login() {
        System.out.println("ENTER ACCOUNT NUMBER:");
        Double accNo = sc.nextDouble();
        System.out.println("ENTER PASSWORD:");
        String enteredPassword = sc.next();

        Customer customer = findCustomer(accNo);
        if (customer != null && customer.getPassword().equals(enteredPassword)) {
            System.out.println("--LOGIN SUCCESSFUL--");
            System.out.println("--WELCOME " + customer.getName()+"--");

            while (true) {
                System.out.println("SELECT OPERATION:\n1. DEPOSIT\n2. WITHDRAW\n3. BALANCE ENQUIRY\n4. LOGOUT");
                int op = sc.nextInt();

                Account account = accounts.get(accNo);
                switch (op) {
                    case 1:
                        System.out.println("ENTER AMOUNT TO DEPOSIT:");
                        double depositAmount = sc.nextDouble();
                        account.deposit(depositAmount);
                        break;
                    case 2:
                        System.out.println("ENTER AMOUNT TO WITHDRAW:");
                        double withdrawAmount = sc.nextDouble();
                        account.withdraw(withdrawAmount);
                        break;
                    case 3:
                        account.showBalance();
                        break;
                    case 4:
                        System.out.println("--LOGGING OUT--");
                        return;
                    default:
                        System.out.println("--INVALID OPERATION--");
                        break;
                }
            }
        } else {
            System.out.println("--INVALID ACCOUNT NUMBER OR PASSWORD--");
            System.out.println("--LOGIN UNSUCCESSFUL!!!--");
        }
    }

    private Customer findCustomer(double accNo) {
        for (Customer c : customers) {
            if (c.getAccNo().equals(accNo)) {
                return c;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {
            System.out.println("***WELCOME TO BANK ACCOUNT MANAGEMENT SYSTEM!!!***");
            System.out.println("1. CREATE NEW ACCOUNT\n2. LOGIN\n3. EXIT");
            System.out.println("ENTER YOUR CHOICE");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    bank.createAccount();
                    break;
                case 2:
                    bank.login();
                    break;
                case 3:
                    System.out.println("**THANK YOU FOR USING OUR BANKING SYSTEM***");
                    sc.close();
                    return;
                default:
                    System.out.println("--INVALID CHOICE--");
                    break;
            }
        }
    }
}