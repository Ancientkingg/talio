package client.scenes;

import client.scenes.components.InfoModal;
import client.services.BoardService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class JoinServerCtrl {

    private final BoardService boardService;

    private final MainCtrl mainCtrl;

    @FXML
    private TextField serverURL;
    private final String defaultURL;

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
        defaultURL = "http://localhost:8080";
    }

    /**
     * Updates the serverIP to
     * @throws IllegalArgumentException if server URL is not valid
     */
    public void joinServer () {

        String url = serverURL.getText();

        if (url == null || url.equals(""))
            url = defaultURL;
        else if (url.startsWith("https://")) {
            final InfoModal errorModal = new InfoModal(boardService, "Invalid Input",
                    "Secure URLs are not supported. Use 'http' instead of 'https'", mainCtrl.getCurrentScene());
            errorModal.showModal();
            return;
        }

        if (!url.startsWith("http://"))
            url = "http://" + url;

        if (!url.matches("/.*:\\d{1,5}/gs"))  // checks if string ends with a colon followed by 1 to 5 digits
            url = url + ":8080"; // if not, add ':8080' as default port number


        try {
            new URL(url).toURI();
        }
        catch (MalformedURLException e) {
            final InfoModal errorModal = new InfoModal(boardService, "Malformed URL",
                    "Entered URL is not a valid URL", mainCtrl.getCurrentScene());
            errorModal.showModal();
            return;
        }
        catch (URISyntaxException e) {
            final InfoModal errorModal = new InfoModal(boardService, "Invalid Input",
                    "Entered text is not a URL", mainCtrl.getCurrentScene());
            errorModal.showModal();
            return;
        }

        boardService.connect(url);
        mainCtrl.showHomePage();
    }
}
