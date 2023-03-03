package client.scenes;

import client.items.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.IOException;

public class JoinBoardCtrl {
    private final MainCtrl mainCtrl;

    private Stage stage;
    private Scene scene;

    @FXML
    private TextField boardName;

    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     */
    @Inject
    public JoinBoardCtrl(final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Allows user to join a board upon inputting the board join code
     * @param e assigned to "join board" button
     * @throws IOException throws IO exception if FXML file not found
     */
    public void joinBoard(final ActionEvent e) throws IOException {
        final Board board = new Board(boardName.getText());
        mainCtrl.addBoard(board);
        mainCtrl.setCurrentBoard(board);
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Routes user to the "Create Board" stage where they can input a board name
     * @param e assigned to "Create Board" button
     * @throws IOException throws IO exception if FXML file not found
     */
    public void createBoard(final ActionEvent e) throws IOException {
        mainCtrl.closeSecondaryStage();
        mainCtrl.showCreateBoard();
    }

    /**
     * Clears fields to avoid accidental repetition of prior arguments
     */
    public void clearFields() {
        boardName.clear();
    }

}
