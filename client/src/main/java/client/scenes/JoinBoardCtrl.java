package client.scenes;

import client.services.BoardService;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class JoinBoardCtrl {
    private final MainCtrl mainCtrl;
    private final BoardService boardService;

    @FXML
    private TextField boardName;

    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     * @param boardService Shared instance of BoardService
     */
    @Inject
    public JoinBoardCtrl(final MainCtrl mainCtrl, final BoardService boardService) {
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
    }

    /**
     * Allows user to join a board upon inputting the board join code
     */
    public void joinBoard() {
        final Board serverBoard = boardService.fetchBoard(boardName.getText());
        boardService.setCurrentBoard(serverBoard);
        mainCtrl.showOverview();
        mainCtrl.refreshOverview();
    }

    /**
     * Routes user to the "Create Board" stage where they can input a board name
     */
    public void createBoard() {
        mainCtrl.showCreateBoard();
    }

    /**
     * Clears fields to avoid accidental repetition of prior arguments
     */
    public void clearFields() {
        boardName.clear();
    }

}
