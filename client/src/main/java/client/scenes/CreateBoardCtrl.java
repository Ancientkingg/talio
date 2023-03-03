package client.scenes;

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
     * upon action event e (create board button press)
     */
    public void createBoard() {
        final Board board = new Board("", boardName.getText(), new TreeSet<>());
        mainCtrl.addBoard(board);
        mainCtrl.setCurrentBoard(board);
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Clears fields to avoid accidental repetition of prior arguments
     */
    public void clearFields() {
        boardName.clear();
    }
}
