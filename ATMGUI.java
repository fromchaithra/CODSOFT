
import javax.swing.*;
import java.awt.*;


class BankAccount {
    private String owner;
    private double balance;
    private final String pin;

    public BankAccount(String owner, double initial, String pin) {
        this.owner = owner;
        this.balance = initial;
        this.pin = pin;
    }

    public boolean authenticate(String attemptPin) {
        return pin.equals(attemptPin);
    }

    public double getBalance() {
        return balance;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) return false;
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) return false;
        balance += amount;
        return true;
    }
}

public class ATMGUI extends JFrame {
    private BankAccount account;
    private JLabel balanceLabel = new JLabel("Balance: ₹0.00");
    private JButton withdrawBtn = new JButton("Withdraw");
    private JButton depositBtn = new JButton("Deposit");
    private JButton checkBtn = new JButton("Check Balance");
    private JButton exitBtn = new JButton("Exit");

    public ATMGUI() {
        super("ATM Interface");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 200);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // For demo purposes, create a default account (in real app, load from DB)
        account = new BankAccount("Demo User", 5000.0, "1234");

        add(balanceLabel);
        add(withdrawBtn);
        add(depositBtn);
        add(checkBtn);
        add(exitBtn);

        withdrawBtn.addActionListener(e -> doWithdraw());
        depositBtn.addActionListener(e -> doDeposit());
        checkBtn.addActionListener(e -> showBalance());
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private boolean ensureAuthenticated() {
        String pin = JOptionPane.showInputDialog(this, "Enter 4-digit PIN:");
        if (pin == null) return false;
        if (!account.authenticate(pin)) {
            JOptionPane.showMessageDialog(this, "Invalid PIN.", "Auth Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void doWithdraw() {
        if (!ensureAuthenticated()) return;
        String s = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (s == null) return;
        double amt;
        try {
            amt = Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter numeric amount.", "Invalid", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (amt <= 0) {
            JOptionPane.showMessageDialog(this, "Amount must be positive.", "Invalid", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!account.withdraw(amt)) {
            JOptionPane.showMessageDialog(this, "Insufficient balance or invalid amount.", "Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Withdrawn ₹" + String.format("%.2f", amt) + " successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        showBalance();
    }

    private void doDeposit() {
        if (!ensureAuthenticated()) return;
        String s = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (s == null) return;
        double amt;
        try {
            amt = Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter numeric amount.", "Invalid", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (amt <= 0) {
            JOptionPane.showMessageDialog(this, "Amount must be positive.", "Invalid", JOptionPane.ERROR_MESSAGE);
            return;
        }
        account.deposit(amt);
        JOptionPane.showMessageDialog(this, "Deposited ₹" + String.format("%.2f", amt) + " successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        showBalance();
    }

    private void showBalance() {
        if (!ensureAuthenticated()) return;
        balanceLabel.setText("Balance: ₹" + String.format("%.2f", account.getBalance()));
        JOptionPane.showMessageDialog(this, "Current balance: ₹" + String.format("%.2f", account.getBalance()), "Balance", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ATMGUI g = new ATMGUI();
            g.setVisible(true);
        });
    }
}
