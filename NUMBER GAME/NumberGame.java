import java.util.Scanner;
import java.util.Random;

public class NumberGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        boolean playAgain = true;
        int totalScore = 0;
        int roundsWon = 0;

        System.out.println("Welcome to the Number Guessing Game!");

        while (playAgain) {
            int lowerBound = 1;
            int upperBound = 100;
            // Generate a random number within the specified range (1 to 100)
            int numberToGuess = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
            int maxAttempts = 10;
            int attempts = 0;
            boolean hasGuessedCorrectly = false;

            System.out.println("\nI have generated a random number between " + lowerBound + " and " + upperBound + ".");
            System.out.println("You have " + maxAttempts + " attempts to guess it.");

            while (attempts < maxAttempts && !hasGuessedCorrectly) {
                System.out.print("Enter your guess: ");
                
                if (!scanner.hasNextInt()) {
                    if (!scanner.hasNext()) { // Check for EOF
                        System.out.println("Input stream closed. Exiting game.");
                        break;
                    }
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.next(); // Consume the invalid input
                    continue;
                }
                
                int userGuess = scanner.nextInt();
                attempts++;

                // Compare the user's guess with the generated number
                if (userGuess == numberToGuess) {
                    System.out.println("Congratulations! You guessed the correct number in " + attempts + " attempts.");
                    hasGuessedCorrectly = true;
                    roundsWon++;
                    // Score calculation based on attempts taken
                    totalScore += (maxAttempts - attempts + 1) * 10; 
                } else if (userGuess < numberToGuess) {
                    System.out.println("Too low! Try again.");
                } else {
                    System.out.println("Too high! Try again.");
                }
            }

            if (!hasGuessedCorrectly) {
                System.out.println("Sorry, you've used all your attempts. The correct number was " + numberToGuess + ".");
            }

            System.out.println("Current Score: " + totalScore + " | Rounds Won: " + roundsWon);
            
            // Ask for multiple rounds
            System.out.print("Do you want to play another round? (yes/no): ");
            if (!scanner.hasNext()) {
                System.out.println("Input stream closed. Exiting game.");
                break;
            }
            String playResponse = scanner.next();
            playAgain = playResponse.equalsIgnoreCase("yes") || playResponse.equalsIgnoreCase("y");
        }

        System.out.println("\nThank you for playing! Final Score: " + totalScore + " | Total Rounds Won: " + roundsWon);
        scanner.close();
    }
}

