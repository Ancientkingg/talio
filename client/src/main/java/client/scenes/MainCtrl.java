package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

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
        overviewCtrl.refresh();
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
