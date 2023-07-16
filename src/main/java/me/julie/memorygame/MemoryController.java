package me.julie.memorygame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MemoryController {
    @FXML
    private Label timeLabel;
    @FXML
    private VBox mainVBox;
    @FXML
    private GridPane grid;
    @FXML
    private Label numLeftLabel;

    private Card[][] cards;
    private int numFlipped;
    private int numLeft;
    private List<Integer> values;
    private List<Color> colors;
    private Random random;
    private Boolean firstCard;
    private Timeline timer;
    private int seconds;

    @FXML
    public void initialize() {
        firstCard = true;
        random = new Random();
        seconds = 0;
        timer = new Timeline(
                new KeyFrame(Duration.seconds(1), (e) -> {
                    seconds++;
                    updateTimer();
                }));
        timer.setCycleCount(Timeline.INDEFINITE);
        updateTimer();
        mainVBox.setStyle("-fx-background-color: #6f6f6f");
        if (Main.getDifficulty().equals(Difficulty.MEDIUM)) { // 20 cells
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.NEVER);
            grid.getColumnConstraints().add(columnConstraints);
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.NEVER);
            grid.getRowConstraints().add(rowConstraints);
            cards = new Card[5][4];
            numLeft = 10;
            values = new ArrayList<>(Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10));
            colors = new ArrayList<>(Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
                    Color.PURPLE, Color.PINK, Color.BROWN, Color.LIGHTBLUE, Color.BEIGE));
        } else if (Main.getDifficulty().equals(Difficulty.HARD)) { // 30 cells
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setHgrow(Priority.NEVER);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setHgrow(Priority.NEVER);
            grid.getColumnConstraints().addAll(col1, col2);
            RowConstraints row1 = new RowConstraints();
            row1.setVgrow(Priority.NEVER);
            RowConstraints row2 = new RowConstraints();
            row2.setVgrow(Priority.NEVER);
            grid.getRowConstraints().addAll(row1, row2);
            cards = new Card[6][5];
            numLeft = 15;
            values = new ArrayList<>(Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10,
                    11, 11, 12, 12, 13, 13, 14, 14, 15, 15));
            colors = new ArrayList<>(Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
                    Color.PURPLE, Color.PINK, Color.BROWN, Color.LIGHTBLUE, Color.GRAY, Color.MAROON,
                    Color.LIGHTGREEN, Color.BEIGE, Color.DARKCYAN, Color.DARKORANGE));
        } else { // easy = (4 x 3) = 12 cells
            cards = new Card[4][3];
            numLeft = 6;
            values = new ArrayList<>(Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6));
            colors = new ArrayList<>(Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
                    Color.PURPLE));
        }
        numLeftLabel.setText(String.valueOf(numLeft));
        setButtons();
    }

    private void updateTimer() {
        int minutes = seconds / 60;
        int newSeconds = seconds % 60;
        String formatted = String.format("%02d:%02d", minutes, newSeconds);
        timeLabel.setText(formatted);
    }

    private void setButtons() {
        for (int i = 0; i < grid.getColumnCount(); i++) {
            for (int j = 0; j < grid.getRowCount(); j++) {
                Button button = new Button();
                button.setFocusTraversable(false);
                GridPane.setFillHeight(button, true);
                GridPane.setFillWidth(button, true);
                button.setPrefSize(500, 500);
                button.setStyle("-fx-background-color: white");
                int finalI = i;
                int finalJ = j;
                button.setOnAction(e -> {
                    flipCard(finalI, finalJ);
                });
                grid.add(button, i, j);
                cards[i][j] = new Card(values.remove(random.nextInt(values.size())));
            }
        }
    }

    private void flipCard(int col, int row) {
        if (firstCard) {
            firstCard = false;
            timer.play();
        }

        if (cards[col][row].isFlipped()) {
            return;
        }

        cards[col][row].flip();
        var color = colors.get(cards[col][row].getValue() - 1);
        String hex = colorToHex(color);

        grid.getChildren().get(col * grid.getRowCount() + row).setStyle("-fx-background-color: " + hex);

        if (++numFlipped == 2) {
            numFlipped = 0;
            checkMatch();
        }

        if (numLeft == 0) {
            timer.stop();
            for (int i = 0; i < grid.getColumnCount(); i++) {
                for (int j = 0; j < grid.getRowCount(); j++) {
                    color = colors.get(cards[i][j].getValue() - 1);
                    hex = colorToHex(color);
                    grid.getChildren().get(i * grid.getRowCount() + j).setStyle("-fx-background-color: " + hex);
                }
            }
            Main.delay(700, () -> {
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.getButtonTypes().add(ButtonType.OK);
                alert.setWidth(80);
                alert.setTitle("Memory Game");
                alert.setContentText("You won!\nLet's play again!\nYour time was: " + timeLabel.getText());
                alert.showAndWait();
                Main.getInstance().loadMenu();
            });
        }
    }

    public static String colorToHex(Color color) {
        int red = (int) Math.round(color.getRed() * 255.0);
        int green = (int) Math.round(color.getGreen() * 255.0);
        int blue = (int) Math.round(color.getBlue() * 255.0);

        String redHex = To00Hex(red);
        String greenHex = To00Hex(green);
        String blueHex = To00Hex(blue);
        String alphaHex = To00Hex(255);

        // hexBinary value: RRGGBBAA
        StringBuilder str = new StringBuilder("#");
        str.append(redHex);
        str.append(greenHex);
        str.append(blueHex);
        str.append(alphaHex);

        return str.toString();
    }

    private static String To00Hex(int value) {
        String hex = "00".concat(Integer.toHexString(value));
        hex = hex.toUpperCase();
        return hex.substring(hex.length() - 2);
    }

    private void checkMatch() {
        Card first = null;
        int firstCol = 0;
        int firstRow = 0;

        for (int col = 0; col < grid.getColumnCount(); col++) {
            for (int row = 0; row < grid.getRowCount(); row++) {
                if (!cards[col][row].isFlipped() || cards[col][row].isMatched()) {
                    continue;
                }
                if (first == null) {
                    first = cards[col][row];
                    firstCol = col;
                    firstRow = row;
                    continue;
                }
                final Card second = cards[col][row];

                if (first.getValue() == second.getValue()) {
                    first.setMatched(true);
                    second.setMatched(true);
                    numLeft--;
                    numLeftLabel.setText(String.valueOf(numLeft));
                    final int finalFirstCol = firstCol;
                    final int finalFirstRow = firstRow;
                    final int finalSecondCol = col;
                    final int finalSecondRow = row;
                    if (numLeft == 0) {
                        return;
                    }
                    Main.delay(700, () -> {
                        grid.getChildren().get(finalFirstCol * grid.getRowCount() + finalFirstRow)
                                .setStyle("-fx-background-color: #6f6f6f"); // same as background
                        grid.getChildren().get(finalSecondCol * grid.getRowCount() + finalSecondRow)
                                .setStyle("-fx-background-color: #6f6f6f");
                    });
                } else {
                    final int finalFirstCol1 = firstCol;
                    final int finalFirstRow1 = firstRow;
                    final int finalSecondCol1 = col;
                    final int finalSecondRow1 = row;
                    cards[firstCol][firstRow].flip();
                    cards[col][row].flip();
                    Main.delay(700, () -> {
                        grid.getChildren().get(finalFirstCol1 * grid.getRowCount() + finalFirstRow1)
                                .setStyle("-fx-background-color: white");
                        grid.getChildren().get(finalSecondCol1 * grid.getRowCount() + finalSecondRow1)
                                .setStyle("-fx-background-color: white");
                    });
                }
                break;
            }
        }
    }
}