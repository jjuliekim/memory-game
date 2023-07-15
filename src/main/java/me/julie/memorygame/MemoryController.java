package me.julie.memorygame;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ArrayList<Integer> values;
    private ArrayList<Color> colors;
    private Random random;

    @FXML
    public void initialize() {
        random = new Random();
        timeLabel.setText("00:00");
        mainVBox.setStyle("-fx-background-color: #6f6f6f");
        if (Main.getDifficulty().equals(Difficulty.MEDIUM)) { // 20 cells
            grid.addColumn(1);
            grid.addRow(1);
            cards = new Card[5][4];
            numLeft = 10;
            values = new ArrayList<>(Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10));
            colors = new ArrayList<>(Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
                    Color.PURPLE, Color.PINK, Color.BROWN, Color.LIGHTBLUE, Color.BEIGE));
        } else if (Main.getDifficulty().equals(Difficulty.HARD)) { // 30 cells
            grid.addColumn(1);
            grid.addColumn(1);
            grid.addRow(1);
            grid.addRow(1);
            cards = new Card[6][5];
            numLeft = 15;
            values = new ArrayList<>(Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10,
                    11, 11, 12, 12, 13, 13, 14, 14, 15, 15));
            colors = new ArrayList<>(Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
                    Color.PURPLE, Color.PINK, Color.BROWN, Color.LIGHTBLUE, Color.GRAY, Color.MAROON,
                    Color.LIGHTGREEN, Color.BEIGE, Color.DARKCYAN, Color.DARKORANGE));
        } else { // easy = 4 x 3 = 12 cells
            cards = new Card[4][3];
            numLeft = 6;
            values = new ArrayList<>(Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6));
            colors = new ArrayList<>(Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
                    Color.PURPLE));
        }
        numLeftLabel.setText(String.valueOf(numLeft));
        setButtons();
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
        Card first;
        Card second;
        int firstK = -1;
        int firstL = -1;

        for (int i = 0; i < grid.getColumnCount(); i++) {
            for (int j = 0; j < grid.getRowCount(); j++) {
                if (cards[i][j].isFlipped() && !(firstK == i && firstL == j)) {
                    first = cards[i][j];

                    System.out.println("first: " + i + ", " + j);
                    for (int k = 0; k < grid.getColumnCount(); k++) {
                        for (int l = 0; l < grid.getRowCount(); l++) {
                            if (cards[k][l].isFlipped() && !(k == i && l == j)) {
                                second = cards[k][l];
                                firstK = k;
                                firstL = l;
                                System.out.println("second: " + k + ", " + l);

                                if (first.getValue() == second.getValue()) {
                                    System.out.println(1);
                                    numLeft--;
                                    numLeftLabel.setText(String.valueOf(numLeft));
                                    int finalI = i;
                                    int finalJ = j;
                                    int finalK = k;
                                    int finalL = l;

                                    Main.delay(500, () -> {
                                        grid.getChildren().get(finalI * grid.getRowCount() + finalJ)
                                                .setStyle("-fx-background-color: #6f6f6f"); // same as background
                                        grid.getChildren().get(finalK * grid.getRowCount() + finalL)
                                                .setStyle("-fx-background-color: #6f6f6f");
                                    });
                                } else {
                                    System.out.println(2);
                                    cards[i][j].flip();
                                    cards[k][l].flip();
                                    int finalI1 = i;
                                    int finalJ1 = j;
                                    int finalL1 = l;
                                    int finalK1 = k;
                                    Main.delay(500, () -> {
                                        grid.getChildren().get(finalI1 * grid.getRowCount() + finalJ1)
                                                .setStyle("-fx-background-color: white");
                                        grid.getChildren().get(finalK1 * grid.getRowCount() + finalL1)
                                                .setStyle("-fx-background-color: white");
                                    });
                                }
                                if (numLeft == 0) {
                                    // stop timer, game over
                                }
                            }
                        }
                    }
                }
            }
        }


    }


}