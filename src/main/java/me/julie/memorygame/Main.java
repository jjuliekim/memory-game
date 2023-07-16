package me.julie.memorygame;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private static Difficulty difficulty;
    private static Main instance;
    private Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        instance = this;
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("menu.fxml"));
        Scene scene = fxmlLoader.load();
        mainStage.setTitle("Memory Game");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Main getInstance() {
        return instance;
    }

    public static Difficulty getDifficulty() {
        return difficulty;
    }

    public static void setDifficulty(Difficulty difficulty) {
        Main.difficulty = difficulty;
    }

    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }

    public void loadMemory() {
        try {
            final Scene scene = FXMLLoader.load(Objects.requireNonNull(getClass()
                    .getResource("memory.fxml")));
            mainStage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadMenu() {
        try {
            final Scene scene = FXMLLoader.load(Objects.requireNonNull(getClass()
                    .getResource("menu.fxml")));
            mainStage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}