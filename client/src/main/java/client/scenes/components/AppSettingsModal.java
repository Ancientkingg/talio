package client.scenes.components;

import client.Main;
import client.scenes.HomePageCtrl;
import client.services.BoardService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.datatransfer.StringSelection;
import java.io.IOException;

public class AppSettingsModal extends Modal {

    private final HomePageCtrl parentCtrl;

    @FXML
    private Text serverURL;

    @FXML
    private Button disconnectFromServer;

    @FXML
    private Button clearCache;

    @FXML
    private Button adminButton;

    @FXML
    private Button exit;


    public AppSettingsModal (final BoardService boardService, final Scene parentScene, final HomePageCtrl parentCtrl) {
        super(boardService, parentScene);
        this.parentCtrl = parentCtrl;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/AppSettingsModal.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize the settings modal
     */
    @FXML
    public void initialize() {
        super.initialize();
        this.serverURL.setText("Server URL"); // TBD - replace with actual url
    }

    /**
     * Disconnect from server
     */
    @FXML
    public void onDisconnectButtonClick () {
        parentCtrl.onDisconnectButtonClick();
    }

    /**
     * deleted saved-boards
     */
    @FXML
    public void deleteSavedBoards () {

    }

    /**
     * switch to god mode
     */
    @FXML
    public void adminMode () {

    }

    /**
     * close the application
     */
    @FXML
    public void exitApplication () {

    }

    /**
     * copy server url to clipboard
     */
    @FXML
    public void onUrlClick() {
        final Point2D p = this.serverURL.localToScreen(110, -32);

        final Tooltip customTooltip = new Tooltip("Copied join-key to clipboard!");
        customTooltip.setStyle("-fx-font-size: 11px");
        customTooltip.setAutoHide(false);
        customTooltip.show(this.serverURL,p.getX(),p.getY());

        final PauseTransition pt = new PauseTransition(Duration.millis(750));
        pt.setOnFinished(e -> {
            customTooltip.hide();
        });
        pt.play();

        final StringSelection joinKeySelection = new StringSelection(boardService.getCurrentBoard().getJoinKey());
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(joinKeySelection, null);

    }
}
