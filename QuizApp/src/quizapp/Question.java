package quizapp;

// Class to hold question details
public class Question {
    String question;
    String[] options = new String[4];
    char correctAnswer;

    public Question(String question, String[] options, char correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}
