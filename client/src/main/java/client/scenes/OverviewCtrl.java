package client.scenes;

import client.items.Board;
import client.items.Column;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class OverviewCtrl {
    private final MainCtrl mainCtrl;

    private Stage stage; //Variables declared globally for future controllers
    private Scene scene;

    private static List<Board> boardList;
    private static Board currentBoard;

    @FXML
    private HBox columnBox;

    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     */
    @Inject
    public OverviewCtrl(final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.boardList = new LinkedList<>();
    }


    /**
     * Redirects user to "createColumn" FXML file on button press
     * where they can input a custom name for a new column to be created.
     * @param e assigned to "create column" button
     * @throws IOException throws IO exception if FXML file not found or other IO mismatch
     */
    public void openColumnInput(final ActionEvent e) throws IOException {
        mainCtrl.showCreateColumn();
    }

    /**
     * Refreshes the overview scene columnBox by iterating over each column in the current board
     * and displaying the corresponding titles. Will also refresh tasks in the future.
     *
     * Is this inefficient? Or does one have to reload all FXML objects to refresh?
     */
    public void refresh() {

        for (final Column col : currentBoard.getColumnList()) {
            final VBox taskBox = new VBox();
            columnBox.getChildren().add(taskBox);

            final Text columnTitle = new Text();
            columnTitle.setText(col.getTitle());
            taskBox.getChildren().add(columnTitle);
        }
    }

    /**
     * Adds board to boardList
     * @param board
     */
    public void addBoard(final Board board) { boardList.add(board); }

    /**
     * Sets the board displayed in overview stage to parameter and loads that board.
     * This method doesn't imply that a displayed board must also be in the boardList of
     * the overviewCtrl, which should always be the case. Depending on implementation later
     * on this may need to be adjusted.
     * @param board Board to be displayed
     */
    public void setCurrentBoard(final Board board) {
        currentBoard = board;
        mainCtrl.refreshOverview();
    }

    /**
     * Gets currently loaded board
     * @return Currently loaded board as Board
     */
    public Board getCurrentBoard() { return currentBoard; }

}
