
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverterGUI extends JFrame {
    private JComboBox<String> fromBox;
    private JComboBox<String> toBox;
    private JTextField amountField = new JTextField(10);
    private JButton convertBtn = new JButton("Convert");
    private JLabel resultLabel = new JLabel("Result: -");

    private static final String[] CURRENCIES = {"USD","EUR","INR","GBP","JPY","AUD","CAD","CNY"};
    private Map<String, Double> rateToINR = new HashMap<>();

    public CurrencyConverterGUI() {
        super("Currency Converter (Offline Version)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 150);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        fromBox = new JComboBox<>(CURRENCIES);
        toBox = new JComboBox<>(CURRENCIES);
        toBox.setSelectedItem("INR");

        // Approximate conversion rates to INR
        rateToINR.put("USD", 83.0);
        rateToINR.put("EUR", 89.0);
        rateToINR.put("INR", 1.0);
        rateToINR.put("GBP", 101.0);
        rateToINR.put("JPY", 0.56);
        rateToINR.put("AUD", 54.0);
        rateToINR.put("CAD", 60.0);
        rateToINR.put("CNY", 11.3);

        add(new JLabel("From:"));
        add(fromBox);
        add(new JLabel("To:"));
        add(toBox);
        add(new JLabel("Amount:"));
        add(amountField);
        add(convertBtn);
        add(resultLabel);

        convertBtn.addActionListener(e -> convert());
    }

    private void convert() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String from = (String) fromBox.getSelectedItem();
            String to = (String) toBox.getSelectedItem();

            double inr = amount * rateToINR.get(from);
            double converted = inr / rateToINR.get(to);

            DecimalFormat df = new DecimalFormat("#,##0.00");
            resultLabel.setText("Result: " + df.format(converted) + " " + to);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CurrencyConverterGUI().setVisible(true));
    }
}
