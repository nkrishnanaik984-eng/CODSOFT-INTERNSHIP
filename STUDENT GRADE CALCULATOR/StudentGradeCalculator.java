import java.util.Scanner;

public class StudentGradeCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Student Grade Calculator ===");
        
        System.out.print("Enter the number of subjects: ");
        int numSubjects = 0;
        if (scanner.hasNextInt()) {
            numSubjects = scanner.nextInt();
        } else {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        if (numSubjects <= 0) {
            System.out.println("The number of subjects must be greater than 0.");
            return;
        }

        int totalMarks = 0;

        for (int i = 1; i <= numSubjects; i++) {
            int marks = -1;
            while (true) {
                System.out.print("Enter marks obtained in subject " + i + " (out of 100): ");
                if (scanner.hasNextInt()) {
                    marks = scanner.nextInt();
                    if (marks >= 0 && marks <= 100) {
                        break;
                    } else {
                        System.out.println("Invalid marks. Please enter a value between 0 and 100.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter an integer value.");
                    scanner.next(); // Clear the invalid input
                }
            }
            totalMarks += marks;
        }

        double averagePercentage = (double) totalMarks / numSubjects;

        String grade;
        if (averagePercentage >= 90) {
            grade = "O (Outstanding)";
        } else if (averagePercentage >= 80) {
            grade = "A+ (Excellent)";
        } else if (averagePercentage >= 70) {
            grade = "A (Very Good)";
        } else if (averagePercentage >= 60) {
            grade = "B+ (Good)";
        } else if (averagePercentage >= 50) {
            grade = "B (Above Average)";
        } else if (averagePercentage >= 40) {
            grade = "C (Average)";
        } else {
            grade = "F (Fail)";
        }

        System.out.println("\n--- final Results ---");
        System.out.println("Total Marks: " + totalMarks + " out of " + (numSubjects * 100));
        System.out.printf("Average Percentage: %.2f%%\n", averagePercentage);
        System.out.println("Grade: " + grade);

        scanner.close();
    }
}
