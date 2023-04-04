package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import static client.Main.SCREEN_SIZE;

public class MainCtrl {


    private Stage primaryStage;

    private JoinServerCtrl joinServerCtrl;

    private Scene joinServerScene;

    private OverviewCtrl overviewCtrl;
    private Scene overviewScene;

    private HomePageCtrl homePageCtrl;
    private Scene homePageScene;



    /**
     * Sets the primary stage upon launch and initializes each controller
     * with its corresponding scene.
     * @param primaryStage This should always be the overview and always be open
     *
     * @param overview The main application page, from which to open other stages
     *
     * @param joinServer The join server page
     *
     * @param homePage The home page
     */
    public void initialize(final Stage primaryStage, final Pair<OverviewCtrl, Parent> overview, final Pair<JoinServerCtrl, Parent> joinServer,
                           final Pair<HomePageCtrl, Parent> homePage)
    {
        this.primaryStage = primaryStage;

        this.joinServerCtrl = joinServer.getKey();
        this.joinServerScene = new Scene(joinServer.getValue());

        this.overviewCtrl = overview.getKey();
        this.overviewScene = new Scene(overview.getValue());

        this.homePageCtrl = homePage.getKey();
        this.homePageScene = new Scene(homePage.getValue());

        showJoinServer();
//        showOverview();
    }

    private void saveStageSize() {
        SCREEN_SIZE[0] = (float) primaryStage.getWidth();
        SCREEN_SIZE[1] = (float) primaryStage.getHeight();
    }

    private void loadStageSize() {
        primaryStage.setWidth(SCREEN_SIZE[0]);
        primaryStage.setHeight(SCREEN_SIZE[1]);

    }

    /**
     * Shows homePage stage in primaryStage
     */
    public void showHomePage() {
        saveStageSize();
        primaryStage.setTitle("Talio: Home Page");
        primaryStage.setScene(homePageScene);
        homePageCtrl.loadBoards();
        loadStageSize();
        primaryStage.show();
    }

    /**
     * Shows overview stage in primaryStage
     */
    public void showOverview() {
        saveStageSize();
        primaryStage.setTitle("Talio: Overview");
        primaryStage.setScene(overviewScene);
        loadStageSize();
        primaryStage.show();
    }


    /**
     * Shows joinServer stage in primaryStage
     */
    public void showJoinServer() {
        saveStageSize();
        primaryStage.setTitle("Talio: Join Server");
        primaryStage.setScene(joinServerScene);
        loadStageSize();
        primaryStage.show();
    }



    /**
     * Closes primaryStage, which should only happen on termination
     */
    public void closePrimaryStage() {
        primaryStage.close();
    }

    /**
     * Gets primary stage
     * @return the primary stage instance
     */
    public Stage getPrimaryStage() { return primaryStage; }


    /**
     * Refreshes overview stage.
     * Is this terribly inefficient or just what it means to refresh by definition?
     */
    public void refreshOverview() {
        overviewCtrl.refreshColumn();
    }


    /**
     * Gets current scene
     * @return current scene that is open
     */
    public Scene getCurrentScene() {
        return primaryStage.getScene();
    }

}
