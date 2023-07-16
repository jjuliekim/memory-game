package me.julie.memorygame;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {
    @FXML
    Button easyButton;
    @FXML
    Button mediumButton;
    @FXML
    Button hardButton;

    @FXML
    public void initialize() {
        easyButton.setFocusTraversable(false);
        mediumButton.setFocusTraversable(false);
        hardButton.setFocusTraversable(false);

        easyButton.setOnAction(e -> {
            Main.setDifficulty(Difficulty.EASY);
            Main.getInstance().loadMemory();
        });
        mediumButton.setOnAction(e -> {
            Main.setDifficulty(Difficulty.MEDIUM);
            Main.getInstance().loadMemory();
        });
        hardButton.setOnAction(e -> {
            Main.setDifficulty(Difficulty.HARD);
            Main.getInstance().loadMemory();
        });
    }
}
