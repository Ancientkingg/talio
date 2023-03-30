package client.scenes.components;

import client.Main;
import client.services.BoardService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class ErrorModal extends Modal {

    @FXML
    private Text titleText;

    @FXML
    private Text descriptionText;


    @FXML
    private Button okButton;

    public ErrorModal(final BoardService boardService, final String title, final String description, final Scene currentScene) {
        super(boardService, title, description, currentScene);
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/ErrorModal.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            titleText.textProperty().set(title);
            descriptionText.textProperty().set(description);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

        okButton.setOnAction(event ->  {
            ((StackPane) currentScene.getRoot()).getChildren().remove(this);
        });

    }
}
