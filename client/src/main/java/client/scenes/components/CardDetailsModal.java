package client.scenes.components;

import client.Main;
import client.services.BoardService;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CardDetailsModal extends Modal {

    private final Card card;

    @FXML
    private TextArea cardTitle;

    @FXML
    private TextArea cardDescription;

    @FXML
    private Button editTitleButton;
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

        //To edit card Title (Seen in overview)

        editTitleButton.setOnAction(e -> cardTitle.setDisable(false)); // Temporarily enable editing of card text

        cardTitle.setOnKeyReleased(e -> { // Disable editing of card text when enter is pressed
            if (e.getCode() == KeyCode.ENTER) {
                cardTitle.setDisable(true);
                card.setTitle(cardTitle.getText());
                refreshTitle();
            }
        });

        //To edit card description

        editDescriptionButton.setOnAction(e -> cardDescription.setDisable(false)); // Temporarily enable editing of card text

        cardDescription.setOnKeyReleased(e -> { // Disable editing of card text when enter is pressed
            if (e.getCode() == KeyCode.ENTER) {
                cardDescription.setDisable(true);
                card.setDescription(cardDescription.getText());
                refreshDescription();
            }
        });

        cardDescription.setDisable(true);
        cardTitle.setDisable(true);
    }

    /**
     * Initialize Modal
     */
    @FXML
    public void initialize() {
        super.initialize();
        this.cardTitle.setText(card.getTitle());
        this.cardDescription.setText(card.getDescription());
    }

    /**
     * Listens for changes in description
     */
    public void listenForDescriptionChanges() {
        cardDescription.focusedProperty().addListener((observable, oldValue, newValue) -> {
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
    }

    /**
     * Listens for changes in title
     */
    public void listenForTitleChanges() {
        cardTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                card.setTitle(cardTitle.getText());
                refreshTitle();
                cardTitle.setDisable(true);
            }
        });

        cardTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            card.setTitle(cardTitle.getText());
            refreshTitle();
        });
    }

    /**
     * Refresh description textArea
     */
    public void refreshDescription() {
        cardDescription.setText(card.getDescription());
    }

    /**
     * Refresh title textArea
     */
    public void refreshTitle() { cardTitle.setText(card.getTitle()); }

    /**
     * Refreshes tags in the scroll pane and displays them in their component form
     */
    public void refreshTags() {
        tagBox.getChildren().removeAll(tagBox.getChildren().stream().filter(c -> c instanceof TagComponent).toList());

        for (final Tag tag : card.getTags()) {
            final TagComponent tagComponent = new TagComponent(boardService, tag.getTitle(), tag.getColorScheme());
            tagBox.getChildren().add(tagBox.getChildren().size() - 1, tagComponent);
        }
    }

}
