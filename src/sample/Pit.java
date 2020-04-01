package sample;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.util.Duration;

import static sample.Controller.*;

public class Pit extends Label {

    private Player player;                          // Who is owning the pit
    private boolean isEndZone;
    private Pit next;                               // Reference to the next pit
    private SimpleIntegerProperty numOfSeeds;
    private Timeline timeline = new Timeline();
    private boolean isGameOver = false;

    public Pit(Player player, Boolean isEndZone) {

        this.player = player;
        this.isEndZone = isEndZone;

        // Style of the label
        this.setStyle("-fx-font-size: 35");

        // Set initial value of seeds in the pit
        if (isEndZone) {
            this.numOfSeeds = new SimpleIntegerProperty(0);
        } else {
            this.numOfSeeds = new SimpleIntegerProperty(6);

            // Change cursor on hover
            this.setCursor(Cursor.CROSSHAIR);

            // Sets event handler for clicking on the pit
            this.setOnMouseClicked(event -> clickOnPit());
        }

        // Binds the number of seeds to the label text
        this.textProperty().bind(numOfSeeds.asString());

    }

    public void setNext(Pit next) {
        this.next = next;
    }

    private void addSeed() {

        // Increment the seed count
        this.numOfSeeds.set(this.numOfSeeds.get() + 1);

        // Scale label for a short time
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), this);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.setToX(1.5);
        scale.setToY(1.5);
        scale.play();
    }

    private void clickOnPit() {

        /*
            - You MUST click one of your own pits (Not if it is a part of an ongoing move)
            - There MUST be seeds in the pit you click
            - Cannot click if there is already an ongoing move
         */
        if (!currentlyMoving && this.numOfSeeds.get() != 0 && (autoMoving || this.player == currentPlayer) && !isGameOver) {

            // Set to prevent more moves before this one is finished
            currentlyMoving = true;

            message.set("Making some moves...");

            // Get number of seeds in the pit that was clicked
            int seeds = this.numOfSeeds.get();

            // Set it to 0 because we picked them up
            this.numOfSeeds.set(0);

            // Set the current pit to the clicked one
            currentPit = this;

            Timeline timeline = new Timeline();   // Timeline is to slow down the game play
            timeline.setCycleCount(seeds);
            timeline.setAutoReverse(false);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), e -> {

                // Get the next pit on the board
                currentPit = currentPit.next;

                // Check if pit is opponents end zone
                if (currentPit.isEndZone) {
                    if (currentPit.player != Controller.currentPlayer) {
                        currentPit = currentPit.next;                     // Skip this pit if it is
                    }
                }

                // Add a seed to the next pit
                currentPit.addSeed();

            }));
            timeline.play();

            timeline.setOnFinished(e -> {
                // Check what should happen after the seeds are distributed
                checkLastPit();
            });
        } else if (!isGameOver) {
            // Displays this whenever an illigal move is made
            message.set(currentPlayer + " you can't do that right now!");
        }

    }

    private void checkLastPit() {
        if (!autoMoving) {
            board.checkIfGameOver();
        }
        currentlyMoving = false;
        autoMoving = false;

        // If you land in your own pit you can continue
        if (!isGameOver) {
            if (currentPit.isEndZone) {
                message.set(currentPlayer + " it is your turn again");
            } else {
                // If you land in an empty pit the other player can play
                if (currentPit.numOfSeeds.get() == 1) {
                    Controller.changeCurrentPlayer();
                    message.set(currentPlayer + " it is now your turn!");
                } else {
                    // AutoMoving is set true, so it is able to take seeds from opponents side
                    autoMoving = true;
                    // Click on the next pit
                    currentPit.clickOnPit();
                }
            }
        }

    }

    public Pit getNext() {
        return next;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isEndZone() {
        return isEndZone;
    }

    public int getNumOfSeeds() {
        return numOfSeeds.get();
    }

    public SimpleIntegerProperty numOfSeedsProperty() {
        return numOfSeeds;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public void setNumOfSeeds(int numOfSeeds) {
        this.numOfSeeds.set(numOfSeeds);
    }
}
