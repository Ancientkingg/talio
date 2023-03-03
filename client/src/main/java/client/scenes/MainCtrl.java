package client.scenes;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

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
        primaryStage.setTitle("Talio: Join Board");
        primaryStage.setScene(joinBoardScene);
        primaryStage.show();
    }

    public void showCreateBoard() {
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Talio: Create Board");
        secondaryStage.setScene(createBoardScene);
        secondaryStage.show();
    }

    public void showCreateColumn() {
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Talio: Create Column");
        secondaryStage.setScene(createColumnScene);
        secondaryStage.show();
    }

}
