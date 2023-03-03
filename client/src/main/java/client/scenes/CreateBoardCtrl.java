package client.scenes;

import client.items.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.IOException;

public class CreateBoardCtrl {
    private final MainCtrl mainCtrl;

    private Stage stage; //variables declared with global scope for future controllers
    private Scene scene;
    private Parent root;

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
     * @param e "create board" button on click
     * @throws IOException thrown if method encounters IO exception/ FXML file is not found
     */
    public void createBoard(final ActionEvent e) throws IOException {
        final Board board = new Board(boardName.getText());
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
