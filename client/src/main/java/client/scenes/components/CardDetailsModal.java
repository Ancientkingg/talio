package client.scenes.components;

import client.Main;
import client.services.BoardService;
import commons.Card;
import commons.Column;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class CardDetailsModal extends Modal {

    private final Card card;

    @FXML
    private Text cardTitle;

    @FXML
    private TextArea cardDescription;

    @FXML
    private Button editDescriptionButton;

    @FXML
    private VBox tagBox;


    public CardDetailsModal(final BoardService boardService, final Scene parentScene, final Card card) {
        super(boardService, parentScene);
        this.card = card;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/CardDetailsModal.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        refreshTags();

        editDescriptionButton.setOnAction(e -> cardDescription.setDisable(false)); // Temporarily enable editing of card text

        cardDescription.setOnKeyReleased(e -> { // Disable editing of card text when enter is pressed
            if (e.getCode() == KeyCode.ENTER) {
                cardDescription.setDisable(true);
                card.setDescription(cardDescription.getText());
                refreshDescription();
            }
        });

        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                card.setDescription(cardDescription.getText());
                refreshDescription();
                cardDescription.setDisable(true);
            }
        });

        cardDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            card.setDescription(cardDescription.getText());
            refreshDescription();
        });

        cardDescription.setDisable(true);
    }

    @FXML
    public void initialize() {
        super.initialize();
        this.cardTitle.setText(card.getTitle());
        this.cardDescription.setText(card.getDescription());
    }

    public void refreshDescription() {
        cardDescription.setText(card.getDescription());
    }

    public void refreshTags() {
        tagBox.getChildren().removeAll(tagBox.getChildren().stream().filter(c -> c instanceof TagComponent).toList());

        for (final Tag tag : card.getTags()) {
            final TagComponent tagComponent = new TagComponent(boardService, tag.getTitle(), tag.getColorScheme());
            tagBox.getChildren().add(tagBox.getChildren().size() - 1, tagComponent);
        }
    }

}
