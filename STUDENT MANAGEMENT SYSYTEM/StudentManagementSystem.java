import java.io.*;
import java.util.*;

public class StudentManagementSystem {

    // ── Student record ───────────────────────────────────────────────────────

    static class Student {
        int rollNumber;
        String name;
        String grade;

        Student(int rollNumber, String name, String grade) {
            this.rollNumber = rollNumber;
            this.name = name;
            this.grade = grade;
        }

        String toCsv() {
            return rollNumber + "," + name + "," + grade;
        }

        static Student fromCsv(String line) {
            String[] parts = line.split(",", 3);
            if (parts.length != 3) return null;
            try {
                int roll = Integer.parseInt(parts[0].trim());
                return new Student(roll, parts[1].trim(), parts[2].trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }

        @Override
        public String toString() {
            return String.format("  Roll: %-6d Name: %-20s Grade: %s",
                                 rollNumber, name, grade);
        }
    }

    // ── In-memory store + persistence ────────────────────────────────────────

    private static final String DATA_FILE = "students.txt";
    private static final List<Student> students = new ArrayList<>();

    private static void loadFromFile() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                Student s = Student.fromCsv(line);
                if (s != null) students.add(s);
            }
        } catch (IOException e) {
            System.out.println("  Warning: could not read data file.");
        }
    }

    private static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Student s : students) pw.println(s.toCsv());
        } catch (IOException e) {
            System.out.println("  Warning: could not save data file.");
        }
    }

    private static Student findByRoll(int roll) {
        for (Student s : students)
            if (s.rollNumber == roll) return s;
        return null;
    }

    // ── UI helpers ───────────────────────────────────────────────────────────

    private static final Scanner sc = new Scanner(System.in);
    private static final String LINE = "-".repeat(50);

    private static String prompt(String label) {
        System.out.print("  " + label + ": ");
        return sc.nextLine().trim();
    }

    private static int promptInt(String label) {
        while (true) {
            String raw = prompt(label);
            try { return Integer.parseInt(raw); }
            catch (NumberFormatException e) {
                System.out.println("  Please enter a valid number.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n" + LINE);
        System.out.println("  STUDENT MANAGEMENT SYSTEM");
        System.out.println(LINE);
        System.out.println("  1. Add student");
        System.out.println("  2. Display all students");
        System.out.println("  3. Search student by roll number");
        System.out.println("  4. Update student");
        System.out.println("  5. Remove student");
        System.out.println("  6. Exit");
        System.out.println(LINE);
    }

    // ── Operations ───────────────────────────────────────────────────────────

    private static void addStudent() {
        System.out.println("\n  -- Add Student --");
        String name = prompt("Name");
        if (name.isEmpty()) { System.out.println("  Error: Name cannot be empty."); return; }

        int roll = promptInt("Roll Number");
        if (roll <= 0) { System.out.println("  Error: Roll number must be positive."); return; }
        if (findByRoll(roll) != null) {
            System.out.println("  Error: Roll number " + roll + " already exists.");
            return;
        }

        String grade = prompt("Grade");
        if (grade.isEmpty()) { System.out.println("  Error: Grade cannot be empty."); return; }

        students.add(new Student(roll, name, grade));
        saveToFile();
        System.out.println("  Student added successfully.");
    }

    private static void displayAll() {
        System.out.println("\n  -- All Students --");
        if (students.isEmpty()) {
            System.out.println("  No student records found.");
            return;
        }
        for (Student s : students) System.out.println(s);
    }

    private static void searchStudent() {
        System.out.println("\n  -- Search Student --");
        int roll = promptInt("Enter Roll Number");
        Student s = findByRoll(roll);
        if (s == null) System.out.println("  Student not found.");
        else           System.out.println(s);
    }

    private static void updateStudent() {
        System.out.println("\n  -- Update Student --");
        int roll = promptInt("Enter Roll Number to update");
        Student s = findByRoll(roll);
        if (s == null) { System.out.println("  Student not found."); return; }

        System.out.println("  Current: " + s);
        System.out.println("  (Press ENTER to keep current value)");

        String name = prompt("New Name [" + s.name + "]");
        String grade = prompt("New Grade [" + s.grade + "]");

        if (!name.isEmpty())  s.name  = name;
        if (!grade.isEmpty()) s.grade = grade;

        saveToFile();
        System.out.println("  Student updated successfully.");
    }

    private static void removeStudent() {
        System.out.println("\n  -- Remove Student --");
        int roll = promptInt("Enter Roll Number to remove");
        Student s = findByRoll(roll);
        if (s == null) { System.out.println("  Student not found."); return; }

        students.remove(s);
        saveToFile();
        System.out.println("  Student removed successfully.");
    }

    // ── Main ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        loadFromFile();
        System.out.println("  " + students.size() + " record(s) loaded.");

        while (true) {
            printMenu();
            String choice = prompt("Enter choice");

            switch (choice) {
                case "1" -> addStudent();
                case "2" -> displayAll();
                case "3" -> searchStudent();
                case "4" -> updateStudent();
                case "5" -> removeStudent();
                case "6" -> { System.out.println("\n  Goodbye!"); sc.close(); return; }
                default  -> System.out.println("  Invalid choice. Enter 1-6.");
            }
        }
    }
}
