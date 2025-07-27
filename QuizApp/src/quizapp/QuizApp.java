package quizapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class QuizApp {
    public static void main(String[] args) {
        try {
            // Load the questions file
            File file = new File("questions.txt");
            Scanner fileScanner = new Scanner(file);
            Scanner input = new Scanner(System.in);

            int score = 0;
            int total = 0;

            // Read up to 5 questions
            while (fileScanner.hasNextLine() && total < 5) {
                String question = fileScanner.nextLine();
                String optionA = fileScanner.nextLine();
                String optionB = fileScanner.nextLine();
                String optionC = fileScanner.nextLine();
                String optionD = fileScanner.nextLine();
                String answer = fileScanner.nextLine().trim().toLowerCase(); // Correct answer (a/b/c/d)

                // Display question and options
                System.out.println("\nQuestion " + (total + 1));
                System.out.println(question);
                System.out.println(optionA);
                System.out.println(optionB);
                System.out.println(optionC);
                System.out.println(optionD);

                // Loop until valid answer is entered
                String userAnswer;
                while (true) {
                    System.out.print("Your answer (a/b/c/d): ");
                    userAnswer = input.nextLine().trim().toLowerCase();

                    if (userAnswer.equals("a") || userAnswer.equals("b") || userAnswer.equals("c") || userAnswer.equals("d")) {
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter a, b, c, or d.");
                    }
                }

                // Track score
                if (userAnswer.equals(answer)) {
                    score++;
                }

                total++;
            }

            // Final result
            System.out.println("\nQuiz complete!");
            System.out.println("Your score: " + score + "/" + total);

            fileScanner.close();
            input.close();

        } catch (FileNotFoundException e) {
            System.out.println("questions.txt not found.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
