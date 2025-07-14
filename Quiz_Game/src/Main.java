import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {
    private Stage primaryStage;
    private QuizDAO quizDAO = new QuizDAO();
    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private String playerName;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showStartScreen();
    }

    private void showStartScreen() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        Label label = new Label("Enter Your Name:");
        TextField nameField = new TextField();
        Button startBtn = new Button("Start Quiz");
        startBtn.setOnAction(e -> {
            playerName = nameField.getText();
            if (!playerName.isEmpty()) {
                startQuiz();
            }
        });
        root.getChildren().addAll(label, nameField, startBtn);
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.setTitle("Quiz Game");
        primaryStage.show();
    }

    private void startQuiz() {
        questions = quizDAO.getRandomQuestions(5);
        currentIndex = 0;
        score = 0;
        showQuestion();
    }

    private void showQuestion() {
        if (currentIndex >= questions.size()) {
            quizDAO.savePlayer(playerName, score);
            showResult();
            return;
        }

        Question q = questions.get(currentIndex);
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        Label questionLabel = new Label((currentIndex+1) + ". " + q.getQuestion());

        ToggleGroup optionsGroup = new ToggleGroup();
        RadioButton a = new RadioButton("A. " + q.getOptionA());
        a.setToggleGroup(optionsGroup);
        RadioButton b = new RadioButton("B. " + q.getOptionB());
        b.setToggleGroup(optionsGroup);
        RadioButton c = new RadioButton("C. " + q.getOptionC());
        c.setToggleGroup(optionsGroup);
        RadioButton d = new RadioButton("D. " + q.getOptionD());
        d.setToggleGroup(optionsGroup);

        Button nextBtn = new Button("Next");
        nextBtn.setOnAction(e -> {
            if (optionsGroup.getSelectedToggle() != null) {
                String selected = ((RadioButton) optionsGroup.getSelectedToggle()).getText().substring(0,1);
                if (selected.equalsIgnoreCase(q.getCorrectOption())) {
                    score++;
                }
                currentIndex++;
                showQuestion();
            }
        });

        root.getChildren().addAll(questionLabel, a, b, c, d, nextBtn);
        primaryStage.setScene(new Scene(root, 400, 300));
    }

    private void showResult() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        Label resultLabel = new Label("Thanks " + playerName + "! Your Score: " + score + "/5");
        Button restartBtn = new Button("Play Again");
        Button exitBtn = new Button("Exit");

        restartBtn.setOnAction(e -> showStartScreen());
        exitBtn.setOnAction(e -> primaryStage.close());

        root.getChildren().addAll(resultLabel, restartBtn, exitBtn);
        primaryStage.setScene(new Scene(root, 400, 300));
    }
}
