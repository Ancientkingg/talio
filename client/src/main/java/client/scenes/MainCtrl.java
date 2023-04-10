package client.scenes;

import client.scenes.components.modals.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import static client.Main.SCREEN_SIZE;

public class MainCtrl {


    private Stage primaryStage;

    private JoinServerCtrl joinServerCtrl;

    private Scene joinServerScene;

    @Getter
    private OverviewCtrl overviewCtrl;
    @Getter
    private Scene overviewScene;

    private HomePageCtrl homePageCtrl;
    private Scene homePageScene;

    @Getter
    @Setter
    private TagsOverviewModal tagsOverviewModal;

    @Getter
    @Setter
    private CardDetailsModal cardDetailsModal;

    @Getter
    @Setter
    private ColorPresetsOverviewModal colorPresetsOverviewModal;

    @Getter
    @Setter
    private BoardSettingsModal boardSettingsModal;

    @Setter
    @Getter
    private TagsShortcutModal tagsShortcutModal;

    @Setter
    @Getter
    private ColorShortcutModal colorShortcutModal;


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
     * Saves the stage size to SCREEN_SIZE
     */
    private void saveStageSize() {
        final float width = (float) primaryStage.getWidth();
        final float height = (float) primaryStage.getHeight();

        SCREEN_SIZE[0] = Float.isNaN(width) ? 1366.0f : width;
        SCREEN_SIZE[1] = Float.isNaN(height) ? 768.0f : height;
    }

    /**
     * Loads the stage size from SCREEN_SIZE
     */
    private void loadStageSize() {
        primaryStage.setWidth(SCREEN_SIZE[0]);
        primaryStage.setHeight(SCREEN_SIZE[1]);

        ( (StackPane) this.homePageScene.getRoot() ).setMinSize(SCREEN_SIZE[0], SCREEN_SIZE[1]);
        ( (StackPane) this.joinServerScene.getRoot() ).setMinSize(SCREEN_SIZE[0], SCREEN_SIZE[1]);
        ( (StackPane) this.overviewScene.getRoot() ).setMinSize(SCREEN_SIZE[0], SCREEN_SIZE[1]);

        ( (StackPane) this.homePageScene.getRoot() ).setPrefSize(SCREEN_SIZE[0], SCREEN_SIZE[1]);
        ( (StackPane) this.joinServerScene.getRoot() ).setPrefSize(SCREEN_SIZE[0], SCREEN_SIZE[1]);
        ( (StackPane) this.overviewScene.getRoot() ).setPrefSize(SCREEN_SIZE[0], SCREEN_SIZE[1]);

        ( (StackPane) this.homePageScene.getRoot() ).setMaxSize(SCREEN_SIZE[0], SCREEN_SIZE[1]);
        ( (StackPane) this.joinServerScene.getRoot() ).setMaxSize(SCREEN_SIZE[0], SCREEN_SIZE[1]);
        ( (StackPane) this.overviewScene.getRoot() ).setMaxSize(SCREEN_SIZE[0], SCREEN_SIZE[1]);

    }

    /**
     * Shows homePage stage in primaryStage
     */
    public void showHomePage() {
        saveStageSize();
        loadStageSize();
        primaryStage.setTitle("Talio: Home Page");
        primaryStage.setScene(homePageScene);
        homePageCtrl.loadBoards();
        homePageCtrl.checkBoards();
        primaryStage.show();
        primaryStage.sizeToScene();
    }

    /**
     * Shows overview stage in primaryStage
     */
    public void showOverview() {
        saveStageSize();
        loadStageSize();
        primaryStage.setTitle("Talio: Overview");
        primaryStage.setScene(overviewScene);
        primaryStage.show();
        primaryStage.sizeToScene();
        overviewCtrl.setKeyboardShortcuts();
    }


    /**
     * Shows joinServer stage in primaryStage
     */
    public void showJoinServer() {
        saveStageSize();
        loadStageSize();
        primaryStage.setTitle("Talio: Join Server");
        primaryStage.setScene(joinServerScene);
        primaryStage.show();
        primaryStage.sizeToScene();
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
        overviewCtrl.refresh();
    }


    /**
     * Gets current scene
     * @return current scene that is open
     */
    public Scene getCurrentScene() {
        return primaryStage.getScene();
    }

    /**
     * refresh a column
     * @param columnId id of column to be refreshed
     */
    public void refreshColumnHeading (final long columnId) {
        overviewCtrl.refreshColumnHeading(columnId);
    }

    /**
     * Gets the color presets overview modal
     * @return the color presets overview modal
     */
    public Refreshable getColorPresetsOverviewModal() {
        return colorPresetsOverviewModal;
    }

    /**
     * Gets the board settings modal
     * @return the board settings modal
     */
    public Refreshable getBoardSettingsModal() {
        return boardSettingsModal;
    }
}
