package client.scenes;

import client.exceptions.BoardChangeException;
import client.models.BoardModel;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.TreeSet;

@Component
public class CreateBoardCtrl {
    private final MainCtrl mainCtrl;
    private final BoardModel boardModel;

    @FXML
    private TextField boardName;

    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     */
    @Inject
    public CreateBoardCtrl(final MainCtrl mainCtrl, final BoardModel boardModel) {
        this.mainCtrl = mainCtrl;
        this.boardModel = boardModel;
    }

    /**
     * Routes the user to the overview of the board they just created
     */
    public void createBoard() throws BoardChangeException {
        final Board board = new Board("", boardName.getText(), new TreeSet<>());
        boardModel.addBoard(board);
        boardModel.setCurrentBoard(board);
        mainCtrl.refreshOverview();
        mainCtrl.showJoinBoard();
    }

    /**
     * Clears fields to avoid accidental repetition of prior arguments
     */
    public void clearFields() {
        boardName.clear();
    }
}
