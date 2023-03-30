package client.scenes;

import client.scenes.components.InfoModal;
import client.services.BoardService;
import jakarta.ws.rs.ProcessingException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import javax.inject.Inject;
import java.nio.channels.UnresolvedAddressException;

public class JoinServerCtrl {

    private final BoardService boardService;

    private final MainCtrl mainCtrl;

    @FXML
    private TextField serverURL;

    /**
     * Injects boardService instance to allow access to methods
     *
     * @param boardService Shared instance of boardService
     * @param mainCtrl Shared instance of mainCtrl
     */
    @Inject
    public JoinServerCtrl (final BoardService boardService, final MainCtrl mainCtrl) {
        this.boardService = boardService;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Updates the serverIP to
     * @throws IllegalArgumentException if server URL is not valid
     */
    public void joinServer () {
        boardService.connect(serverURL.getText());
        mainCtrl.showHomePage();
    }
}
