package client.scenes;

import client.models.BoardModel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.inject.Inject;

public class MainCtrl {


    private final BoardModel boardModel;
    private Stage primaryStage;
    private JoinBoardCtrl joinBoardCtrl;
    private Scene joinBoardScene;
    private OverviewCtrl overviewCtrl;
    private Scene overviewScene;
    private CreateColumnCtrl createColumnCtrl;
    private Scene createColumnScene;
    private CreateBoardCtrl createBoardCtrl;
    private Scene createBoardScene;
    private CreateCardCtrl createCardCtrl;
    private Scene createCardScene;

    @Inject
    public MainCtrl(final BoardModel boardModel) {
        this.boardModel = boardModel;
    }

    /**
     * Sets the primary stage upon launch and initializes each controller
     * with its corresponding scene.
     * @param primaryStage This should always be the overview and always be open
     * @param overview The main application page, from which to open other stages
     * @param joinBoard Join board page will initially be open upon booting to ensure some board
     *                  exists by the time one arrives at the overview
     * @param createBoard Create board page is an option from the join board page and creates
     *                    a new board which is loaded into the overview
     * @param createColumn The create column page is an option to add a column to a board
     *                     in the overview
     * @param createCard The create card page is an option to add a card to a column
     *                     in the overview
     */
    public void initialize(final Stage primaryStage, final Pair<OverviewCtrl, Parent> overview, final Pair<JoinBoardCtrl, Parent> joinBoard,
                           final Pair<CreateBoardCtrl, Parent> createBoard, final Pair<CreateColumnCtrl, Parent> createColumn,
                           final Pair<CreateCardCtrl, Parent> createCard)
    {
        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overviewScene = new Scene(overview.getValue());
        this.joinBoardCtrl = joinBoard.getKey();
        this.joinBoardScene = new Scene(joinBoard.getValue());
        this.createBoardCtrl = createBoard.getKey();
        this.createBoardScene = new Scene(createBoard.getValue());
        this.createColumnCtrl = createColumn.getKey();
        this.createColumnScene = new Scene(createColumn.getValue());
        this.createCardCtrl = createCard.getKey();
        this.createCardScene = new Scene(createCard.getValue());

        showJoinBoard();
    }

    /**
     * Shows overview stage in primaryStage
     */
    public void showOverview() {
        primaryStage.setTitle("Talio: Overview");
        primaryStage.setScene(overviewScene);
        primaryStage.show();
    }

    /**
     * Shows joinBoard stage in primaryStage
     */
    public void showJoinBoard() {
        joinBoardCtrl.clearFields();
        primaryStage.setTitle("Talio: Join Board");
        primaryStage.setScene(joinBoardScene);
        primaryStage.show();
    }

    /**
     * Shows createBoard stage in primaryStage
     */
    public void showCreateBoard() {
        createBoardCtrl.clearFields();
        primaryStage.setTitle("Talio: Create Board");
        primaryStage.setScene(createBoardScene);
        primaryStage.show();
    }

    /**
     * Shows createColumn stage in primaryStage
     */
    public void showCreateColumn() {
        createColumnCtrl.clearFields();
        primaryStage.setTitle("Talio: Create Column");
        primaryStage.setScene(createColumnScene);
        primaryStage.show();
    }

    /**
     * Shows createColumn stage in primaryStage
     */
    public void showCreateCard() {
//        createCardCtrl.clearFields();
//        createCardCtrl.loadMenuItems();
//        primaryStage.setTitle("Talio: Create Card");
//        primaryStage.setScene(createCardScene);
//        primaryStage.show();
    }


    /**
     * Closes primaryStage, which should only happen on termination
     */
    public void closePrimaryStage() {
        primaryStage.close();
    }


    /**
     * Refreshes overview stage.
     * Is this terribly inefficient or just what it means to refresh by definition?
     */
    public void refreshOverview() {
        overviewCtrl.refresh();
    }

}
