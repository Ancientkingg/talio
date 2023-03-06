package client.scenes;

import client.exceptions.BoardChangeException;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.util.TreeSet;

public class JoinBoardCtrl {
    private final MainCtrl mainCtrl;

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
     */
    public void joinBoard() throws BoardChangeException {
        final Board board = new Board(boardName.getText(), "title", new TreeSet<>());
        mainCtrl.addBoard(board);
        mainCtrl.setCurrentBoard(board);
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Routes user to the "Create Board" stage where they can input a board name
     */
    public void createBoard() {
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
