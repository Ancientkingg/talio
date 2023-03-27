package client.scenes;

import client.exceptions.BoardChangeException;
import client.services.BoardService;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.TreeSet;

@Component
public class CreateBoardCtrl {
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
    public CreateBoardCtrl(final MainCtrl mainCtrl, final BoardService boardService) {
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
    }

    /**
     * Routes the user to the overview of the board they just created
     */
    public void createBoard() throws BoardChangeException {
        final Board board = new Board(null, boardName.getText(), new TreeSet<>());
        final Board serverBoard = boardService.addBoard(board);
        boardService.setCurrentBoard(serverBoard);
        mainCtrl.showOverview();
    }

    /**
     * Clears fields to avoid accidental repetition of prior arguments
     */
    public void clearFields() {
        boardName.clear();
    }
}
