package client.scenes;

import client.exceptions.BoardChangeException;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.util.TreeSet;

public class CreateBoardCtrl {
    private final MainCtrl mainCtrl;

    @FXML
    private TextField boardName;

    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     */
    @Inject
    public CreateBoardCtrl(final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Routes the user to the overview of the board they just created
     */
    public void createBoard() throws BoardChangeException {
        final Board board = new Board("", boardName.getText(), new TreeSet<>());
        mainCtrl.addBoard(board);
        mainCtrl.setCurrentBoard(board);
        final Column col = new Column("Default Header", 0, new TreeSet<>());
        mainCtrl.addColumn(col);
        mainCtrl.refreshOverview();
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Clears fields to avoid accidental repetition of prior arguments
     */
    public void clearFields() {
        boardName.clear();
    }
}
