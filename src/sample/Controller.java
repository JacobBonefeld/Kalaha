package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public AnchorPane pane;
    @FXML
    public Label playerLabel;

    public static Board board;
    public static boolean currentlyMoving =  false;
    public static boolean autoMoving = false;
    public static Player currentPlayer =  Player.PLAYER1;
    public static Pit currentPit;
    public static SimpleStringProperty message = new SimpleStringProperty("Player 1 - Your turn");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board = new Board(pane);
        playerLabel.textProperty().bind(message);
        playerLabel.setStyle("-fx-font-size: 20");
    }

    public static void changeCurrentPlayer(){

        if(currentPlayer == Player.PLAYER1) currentPlayer =  Player.PLAYER2;
        else currentPlayer = Player.PLAYER1;

    }



}
