package client.scenes.components;

import client.Main;
import client.services.BoardService;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class InfoModal extends Modal implements UIComponent {

    @FXML
    private Text titleText;

    @FXML
    private Text descriptionText;

    private final String title;

    private final String description;


    @FXML
    private Button okButton;

    /**
     * Constructor for InfoModal
     * @param boardService boardService instance
     * @param title title of infomodal
     * @param description description of infomodal
     * @param currentScene current scene (displayed under modal)
     */
    public InfoModal(final BoardService boardService, final String title, final String description, final Scene currentScene) {
        super(boardService, currentScene);
        this.title = title;
        this.description = description;

        loadSource(Main.class.getResource("/components/InfoModal.fxml"));

        try {
            titleText.textProperty().set(title);
            descriptionText.textProperty().set(description);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

        okButton.setOnAction(event ->  {
            super.closeModal();
        });

    }
}
