
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class NumberGameGUI extends JFrame {
    private int target;
    private int attemptsLeft;
    private int totalScore;
    private int roundsPlayed;
    private final Random rand = new Random();

    private JLabel infoLabel = new JLabel("Guess a number between 1 and 100");
    private JTextField guessField = new JTextField(10);
    private JButton guessBtn = new JButton("Guess");
    private JButton newRoundBtn = new JButton("New Round");
    private JLabel statusLabel = new JLabel("Attempts left: -");
    private JLabel scoreLabel = new JLabel("Score: 0 | Rounds: 0");

    public NumberGameGUI() {
        super("Number Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 180);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        attemptsLeft = 7; // default attempts per round
        startNewRound();

        JPanel top = new JPanel();
        top.add(infoLabel);

        JPanel mid = new JPanel();
        mid.add(new JLabel("Your Guess:"));
        mid.add(guessField);
        mid.add(guessBtn);

        JPanel bottom = new JPanel();
        bottom.add(statusLabel);
        bottom.add(newRoundBtn);
        bottom.add(scoreLabel);

        c.gridx = 0; c.gridy = 0; add(top, c);
        c.gridx = 0; c.gridy = 1; add(mid, c);
        c.gridx = 0; c.gridy = 2; add(bottom, c);

        guessBtn.addActionListener(e -> handleGuess());
        guessField.addActionListener(e -> handleGuess());
        newRoundBtn.addActionListener(e -> {
            startNewRound();
            updateUIState();
        });

        updateUIState();
    }

    private void startNewRound() {
        target = rand.nextInt(100) + 1;
        attemptsLeft = 7;
        infoLabel.setText("Guess a number between 1 and 100");
        statusLabel.setText("Attempts left: " + attemptsLeft);
        guessField.setText("");
    }

    private void handleGuess() {
        String s = guessField.getText().trim();
        if (s.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a guess.", "Input needed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int guess;
        try {
            guess = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid integer.", "Invalid", JOptionPane.ERROR_MESSAGE);
            return;
        }
        attemptsLeft--;
        if (guess == target) {
            int earned = Math.max(0, 10 + attemptsLeft * 2); // scoring heuristic
            totalScore += earned;
            roundsPlayed++;
            JOptionPane.showMessageDialog(this, "Correct! You scored " + earned + " points.", "Winner", JOptionPane.INFORMATION_MESSAGE);
            startNewRound();
        } else if (guess < target) {
            infoLabel.setText("Too low! Try higher.");
        } else {
            infoLabel.setText("Too high! Try lower.");
        }

        if (attemptsLeft <= 0) {
            roundsPlayed++;
            JOptionPane.showMessageDialog(this, "Out of attempts! The number was: " + target, "Round Over", JOptionPane.INFORMATION_MESSAGE);
            startNewRound();
        }
        updateUIState();
    }

    private void updateUIState() {
        statusLabel.setText("Attempts left: " + attemptsLeft);
        scoreLabel.setText("Score: " + totalScore + " | Rounds: " + roundsPlayed);
        guessField.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NumberGameGUI g = new NumberGameGUI();
            g.setVisible(true);
        });
    }
}
