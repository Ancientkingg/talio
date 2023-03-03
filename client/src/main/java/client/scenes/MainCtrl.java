package client.scenes;

import client.items.Board;
import client.items.Column;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;
    private Stage secondaryStage;

    private JoinBoardCtrl joinBoardCtrl;
    private Scene joinBoardScene;

    private OverviewCtrl overviewCtrl;
    private Scene overviewScene;

    private CreateColumnCtrl createColumnCtrl;
    private Scene createColumnScene;

    private CreateBoardCtrl createBoardCtrl;
    private Scene createBoardScene;

    public void initialize(final Stage primaryStage, Pair<OverviewCtrl, Parent> overview, Pair<JoinBoardCtrl, Parent> joinBoard,
                      Pair<CreateBoardCtrl, Parent> createBoard, Pair<CreateColumnCtrl, Parent> createColumn){
        this.primaryStage = primaryStage;
        this.secondaryStage = new Stage();

        this.overviewCtrl = overview.getKey();
        this.overviewScene = new Scene(overview.getValue());
        this.joinBoardCtrl = joinBoard.getKey();
        this.joinBoardScene = new Scene(joinBoard.getValue());
        this.createBoardCtrl = createBoard.getKey();
        this.createBoardScene = new Scene(createBoard.getValue());
        this.createColumnCtrl = createColumn.getKey();
        this.createColumnScene = new Scene(createColumn.getValue());

        showJoinBoard();
        primaryStage.show();
    }

    public void showOverview() {
        primaryStage.setTitle("Talio: Overview");
        primaryStage.setScene(overviewScene);
        primaryStage.show();
    }

    public void showJoinBoard() {
        joinBoardCtrl.clearFields();
        primaryStage.setTitle("Talio: Join Board");
        primaryStage.setScene(joinBoardScene);
        primaryStage.show();
    }

    public void showCreateBoard() {
        createBoardCtrl.clearFields();
        secondaryStage.setTitle("Talio: Create Board");
        secondaryStage.setScene(createBoardScene);
        secondaryStage.show();
    }

    public void showCreateColumn() {
        createColumnCtrl.clearFields();
        secondaryStage.setTitle("Talio: Create Column");
        secondaryStage.setScene(createColumnScene);
        secondaryStage.show();
    }

    public void closeSecondaryStage() {
        secondaryStage.close();
    }

    public void closePrimaryStage() {
        primaryStage.close();
    }

    public void addColumn(Column col) {
        overviewCtrl.getCurrentBoard().addColumn(col);
    }

    public void refreshOverview() {
        overviewCtrl.refresh();
    }

    public void addBoard(Board board) {
        overviewCtrl.addBoard(board);
    }

    public void setCurrentBoard(Board board) {
        overviewCtrl.setCurrentBoard(board);
    }
}
