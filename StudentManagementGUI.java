
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String rollNo;
    private String grade;
    private String extra;

    public Student(String name, String rollNo, String grade, String extra) {
        this.name = name;
        this.rollNo = rollNo;
        this.grade = grade;
        this.extra = extra;
    }

    public String getName() { return name; }
    public String getRollNo() { return rollNo; }
    public String getGrade() { return grade; }
    public String getExtra() { return extra; }

    public void setName(String name) { this.name = name; }
    public void setRollNo(String r) { this.rollNo = r; }
    public void setGrade(String g) { this.grade = g; }
    public void setExtra(String e) { this.extra = e; }

    public String toString() {
        return rollNo + " - " + name + " | Grade: " + grade;
    }
}

public class StudentManagementGUI extends JFrame {
    private DefaultListModel<Student> model = new DefaultListModel<>();
    private JList<Student> list = new JList<>(model);
    private final File storeFile = new File("students.dat");

    public StudentManagementGUI() {
        super("Student Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8,8));

        JPanel left = new JPanel(new BorderLayout());
        left.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel controls = new JPanel(new GridLayout(0,1,5,5));
        JButton addBtn = new JButton("Add Student");
        JButton editBtn = new JButton("Edit Selected");
        JButton delBtn = new JButton("Delete Selected");
        JButton searchBtn = new JButton("Search by Roll");
        JButton saveBtn = new JButton("Save to File");
        JButton loadBtn = new JButton("Load from File");

        controls.add(addBtn);
        controls.add(editBtn);
        controls.add(delBtn);
        controls.add(searchBtn);
        controls.add(saveBtn);
        controls.add(loadBtn);

        left.add(controls, BorderLayout.SOUTH);

        add(left, BorderLayout.WEST);

        JTextArea detailArea = new JTextArea();
        detailArea.setEditable(false);
        add(new JScrollPane(detailArea), BorderLayout.CENTER);

        list.addListSelectionListener(e -> {
            Student s = list.getSelectedValue();
            if (s != null) {
                detailArea.setText("Name: " + s.getName() + "\nRoll No: " + s.getRollNo() + "\nGrade: " + s.getGrade() + "\nExtra: " + s.getExtra());
            } else detailArea.setText("");
        });

        addBtn.addActionListener(e -> {
            Student s = showStudentDialog(null);
            if (s != null) model.addElement(s);
        });

        editBtn.addActionListener(e -> {
            Student sel = list.getSelectedValue();
            if (sel == null) { JOptionPane.showMessageDialog(this, "Select a student first."); return; }
            Student updated = showStudentDialog(sel);
            if (updated != null) {
                sel.setName(updated.getName());
                sel.setRollNo(updated.getRollNo());
                sel.setGrade(updated.getGrade());
                sel.setExtra(updated.getExtra());
                list.repaint();
            }
        });

        delBtn.addActionListener(e -> {
            Student sel = list.getSelectedValue();
            if (sel == null) { JOptionPane.showMessageDialog(this, "Select a student first."); return; }
            int c = JOptionPane.showConfirmDialog(this, "Delete " + sel.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) model.removeElement(sel);
        });

        searchBtn.addActionListener(e -> {
            String roll = JOptionPane.showInputDialog(this, "Enter roll number to search:");
            if (roll == null || roll.trim().isEmpty()) return;
            for (int i = 0; i < model.size(); i++) {
                if (model.get(i).getRollNo().equalsIgnoreCase(roll.trim())) {
                    list.setSelectedIndex(i);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Student not found.");
        });

        saveBtn.addActionListener(e -> saveToFile());
        loadBtn.addActionListener(e -> loadFromFile());

        // Load if exists
        if (storeFile.exists()) loadFromFile();
    }

    private Student showStudentDialog(Student existing) {
        JTextField nameF = new JTextField();
        JTextField rollF = new JTextField();
        JTextField gradeF = new JTextField();
        JTextField extraF = new JTextField();
        if (existing != null) {
            nameF.setText(existing.getName());
            rollF.setText(existing.getRollNo());
            gradeF.setText(existing.getGrade());
            extraF.setText(existing.getExtra());
        }
        Object[] fields = {
            "Name:", nameF,
            "Roll No:", rollF,
            "Grade:", gradeF,
            "Extra Info:", extraF
        };
        int ok = JOptionPane.showConfirmDialog(this, fields, existing == null ? "Add Student" : "Edit Student", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            String name = nameF.getText().trim();
            String roll = rollF.getText().trim();
            String grade = gradeF.getText().trim();
            String extra = extraF.getText().trim();
            if (name.isEmpty() || roll.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Roll No are required.");
                return null;
            }
            return new Student(name, roll, grade.isEmpty() ? "-" : grade, extra.isEmpty() ? "-" : extra);
        }
        return null;
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeFile))) {
            ArrayList<Student> arr = new ArrayList<>();
            for (int i = 0; i < model.size(); i++) arr.add(model.get(i));
            oos.writeObject(arr);
            JOptionPane.showMessageDialog(this, "Saved to " + storeFile.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        if (!storeFile.exists()) {
            JOptionPane.showMessageDialog(this, "No saved file found.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeFile))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList) {
                ArrayList<Student> arr = (ArrayList<Student>) obj;
                model.clear();
                for (Student s : arr) model.addElement(s);
                JOptionPane.showMessageDialog(this, "Loaded " + arr.size() + " students.");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid file format.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Load failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementGUI g = new StudentManagementGUI();
            g.setVisible(true);
        });
    }
}
