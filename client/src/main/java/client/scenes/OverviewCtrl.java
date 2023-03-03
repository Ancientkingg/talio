package client.scenes;

import client.items.Board;
import client.items.Column;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    @Inject
    public OverviewCtrl(MainCtrl mainCtrl) {
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

    public void refresh(){

        for (Column col : currentBoard.getColumnList()){
            VBox taskBox = new VBox();
            columnBox.getChildren().add(taskBox);

            Text columnTitle = new Text();
            columnTitle.setText(col.getTitle());
            taskBox.getChildren().add(columnTitle);
        }
    }

    public void addBoard(Board board) { boardList.add(board); }

    public void setCurrentBoard(Board board) { currentBoard = board; }

    public Board getCurrentBoard(){ return currentBoard; }

}
