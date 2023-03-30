package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {


    private Stage primaryStage;
    private JoinBoardCtrl joinBoardCtrl;
    private Scene joinBoardScene;

    private JoinServerCtrl joinServerCtrl;

    private Scene joinServerScene;
    private OverviewCtrl overviewCtrl;
    private Scene overviewScene;
    private Scene createColumnScene;
    private CreateBoardCtrl createBoardCtrl;
    private Scene createBoardScene;
    private CreateCardCtrl createCardCtrl;
    private Scene createCardScene;
    private HomePageCtrl homePageCtrl;
    private Scene homePageScene;



    /**
     * Sets the primary stage upon launch and initializes each controller
     * with its corresponding scene.
     * @param primaryStage This should always be the overview and always be open
     * @param overview The main application page, from which to open other stages
     * @param joinBoard Join board page will initially be open upon booting to ensure some board
     *                  exists by the time one arrives at the overview
     * @param createBoard Create board page is an option from the join board page and creates
     *                    a new board which is loaded into the overview
     * @param createCard The create card page is an option to add a card to a column
     *                     in the overview
     *
     * @param joinServer The join server page
     *
     * @param homePage The home page
     */
    public void initialize(final Stage primaryStage, final Pair<OverviewCtrl, Parent> overview, final Pair<JoinBoardCtrl, Parent> joinBoard,
                           final Pair<CreateBoardCtrl, Parent> createBoard,
                           final Pair<CreateCardCtrl, Parent> createCard, final Pair<JoinServerCtrl, Parent> joinServer,
                           final Pair<HomePageCtrl, Parent> homePage)
    {
        this.primaryStage = primaryStage;
        this.joinServerCtrl = joinServer.getKey();
        this.joinServerScene = new Scene(joinServer.getValue());


        this.overviewCtrl = overview.getKey();
        this.overviewScene = new Scene(overview.getValue());
        this.joinBoardCtrl = joinBoard.getKey();
        this.joinBoardScene = new Scene(joinBoard.getValue());
        this.createBoardCtrl = createBoard.getKey();
        this.createBoardScene = new Scene(createBoard.getValue());
        this.createCardCtrl = createCard.getKey();
        this.createCardScene = new Scene(createCard.getValue());
        this.homePageCtrl = homePage.getKey();
        this.homePageScene = new Scene(homePage.getValue());

        showJoinServer();
//        showOverview();
    }

    /**
     * Shows homePage stage in primaryStage
     */
    public void showHomePage() {
        primaryStage.setTitle("Talio: Home Page");
        primaryStage.setScene(homePageScene);
        homePageCtrl.loadBoards();
        primaryStage.show();
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
     * Shows joinServer stage in primaryStage
     */
    public void showJoinServer() {
        primaryStage.setTitle("Talio: Join Server");
        primaryStage.setScene(joinServerScene);
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
        overviewCtrl.refreshColumn();
    }

}
