
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GradeCalculatorGUI extends JFrame {
    private JSpinner subjectCountSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));
    private JPanel marksPanel = new JPanel(new GridLayout(0, 2, 5, 5));
    private ArrayList<JTextField> markFields = new ArrayList<>();
    private JButton calculateBtn = new JButton("Calculate");
    private JTextArea resultArea = new JTextArea(6, 30);

    public GradeCalculatorGUI() {
        super("Student Grade Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 360);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel();
        top.add(new JLabel("Number of subjects: "));
        top.add(subjectCountSpinner);
        JButton setBtn = new JButton("Set");
        top.add(setBtn);
        add(top, BorderLayout.NORTH);

        JScrollPane centerScroll = new JScrollPane(marksPanel);
        add(centerScroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.add(calculateBtn);
        add(bottom, BorderLayout.SOUTH);

        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.EAST);

        setFields((Integer) subjectCountSpinner.getValue());

        setBtn.addActionListener(e -> setFields((Integer) subjectCountSpinner.getValue()));
        calculateBtn.addActionListener(e -> calculate());

    }

    private void setFields(int n) {
        marksPanel.removeAll();
        markFields.clear();
        for (int i = 1; i <= n; i++) {
            marksPanel.add(new JLabel("Subject " + i + " (0-100):"));
            JTextField f = new JTextField();
            markFields.add(f);
            marksPanel.add(f);
        }
        marksPanel.revalidate();
        marksPanel.repaint();
    }

    private void calculate() {
        int total = 0;
        int n = markFields.size();
        for (int i = 0; i < n; i++) {
            String s = markFields.get(i).getText().trim();
            if (s.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter all marks.", "Missing", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int m;
            try {
                m = Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Provide numeric marks only.", "Invalid", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (m < 0 || m > 100) {
                JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100.", "Invalid", JOptionPane.ERROR_MESSAGE);
                return;
            }
            total += m;
        }
        double average = (double) total / n;
        String grade = getGrade(average);
        StringBuilder sb = new StringBuilder();
        sb.append("Total Marks: ").append(total).append("\n");
        sb.append("Average: ").append(String.format("%.2f", average)).append("%\n");
        sb.append("Grade: ").append(grade).append("\n");
        resultArea.setText(sb.toString());
    }

    private String getGrade(double avg) {
        if (avg >= 90) return "A+";
        if (avg >= 80) return "A";
        if (avg >= 70) return "B+";
        if (avg >= 60) return "B";
        if (avg >= 50) return "C";
        return "F";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GradeCalculatorGUI g = new GradeCalculatorGUI();
            g.setVisible(true);
        });
    }
}
