package sample;

import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;

public class Board {

    int topPoints=0;
    int bottomPoints=0;
    public AnchorPane pane;
    public Pit head = null;
    public Pit tail = null;

    public Board(AnchorPane pane) {
        this.pane = pane;
        this.addPits();
    }

    public void add(Pit pit) {

        if (head == null) {
            head = pit;
            tail = pit;
            pit.setNext(pit);
        } else {
            tail.setNext(pit);
            tail = pit;
            tail.setNext(head);
        }
    }

    private void addPits() {

        // Player 1 - houses
        for (int j = 95; j <= 462; j += 69) {

            Pit newPit = new Pit(Player.PLAYER1, false);
            newPit.setLayoutX(j);
            newPit.setLayoutY(247);
            newPit.setAlignment(Pos.CENTER);
            newPit.setPrefWidth(60);
            newPit.setPrefHeight(75);

            pane.getChildren().add(newPit);

            this.add(newPit);
        }

        // Player 1 - end zone
        addEndZone(530,Player.PLAYER1);

        // Player 2 - houses
        for (int j = 440; j >= 50; j -= 69) {

            Pit newPit = new Pit(Player.PLAYER2, false);
            newPit.setLayoutX(j);
            newPit.setLayoutY(30);
            newPit.setAlignment(Pos.CENTER);
            newPit.setPrefWidth(60);
            newPit.setPrefHeight(75);
            pane.getChildren().add(newPit);

            this.add(newPit);
        }

        // Player 2 - end zone
        addEndZone(45,Player.PLAYER2);
    }

    private void addEndZone(int layoutX, Player player){

        final int LAYOUT_Y = 150;

        Pit newPit = new Pit(player,true);
        newPit.setLayoutY(LAYOUT_Y);
        newPit.setLayoutX(layoutX);
        pane.getChildren().add(newPit);

        this.add(newPit);
    }

    public boolean checkIfGameOver() {
        boolean gameIsOver =false;
        topPoints=0;
        bottomPoints=0;
        Pit currentPit = head;
        if (head != null) {
            do {
                if (!currentPit.isEndZone() && currentPit.getPlayer().equals(Player.PLAYER1)) {
                    bottomPoints += currentPit.getNumOfSeeds();
                } else if (!currentPit.isEndZone() && currentPit.getPlayer().equals(Player.PLAYER2)) {
                    topPoints += currentPit.getNumOfSeeds();
                }
                currentPit = currentPit.getNext();
            } while (currentPit != head);
        }
        System.out.println(bottomPoints + ":   " + topPoints + ":  ");
        if (topPoints==0 || bottomPoints==0){
            System.out.println("GameEnded");
            gameIsOver = true;
            stopTheGame();
        }
        return gameIsOver;
    }

    private void stopTheGame() {
        Pit currentPit = head;
        if (head != null) {
            do {
                if (currentPit.getPlayer()==Player.PLAYER1 && currentPit.isEndZone()){
                    bottomPoints+= currentPit.getNumOfSeeds();
                    currentPit.setNumOfSeeds(bottomPoints);
                } else if ((currentPit.getPlayer()==Player.PLAYER2 && currentPit.isEndZone())){
                    topPoints += currentPit.getNumOfSeeds();
                    currentPit.setNumOfSeeds(topPoints);
                } else{
                    currentPit.setNumOfSeeds(0);
                }


                currentPit.setGameOver(true);
                currentPit = currentPit.getNext();
            } while (currentPit != head);
        }
        Controller.gameOver();
    }

    public int getTopPoints() {
        return topPoints;
    }

    public int getBottomPoints() {
        return bottomPoints;
    }
}
