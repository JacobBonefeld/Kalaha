package sample;

import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;

public class Board {

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


}
