package client.scenes.components.modals;

import client.Main;
import client.scenes.LiveUIController;
import client.scenes.components.CardComponent;
import client.scenes.components.TagComponent;
import client.services.BoardService;
import commons.Board;
import commons.Card;
import commons.ColorScheme;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class CardDetailsModal extends Modal implements LiveUIController {

    private final Card card;

    private final CardComponent cardComponent;

    @FXML
    private TextField cardTitle;

    @FXML
    private TextArea cardDescription;

    @FXML
    private VBox tagsContainer;

    @FXML
    private VBox subTasksContainer;

    @FXML
    private ComboBox<ColorScheme> colorSchemeComboBox;

    /**
     * Constructor for card details modal
     * @param boardService shared boardService instance
     * @param parentScene the scene over which the modal has to overlay
     * @param card the card from which the modal is called
     * @param cardComponent parent cardComponent
     */
    public CardDetailsModal(final BoardService boardService, final Scene parentScene, final Card card, final CardComponent cardComponent) {
        super(boardService, parentScene);
        this.card = card;
        this.cardComponent = cardComponent;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/CardDetailsModal.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        refresh();

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
     * Submits data and refreshes overview when submit button is pressed;
     */
    @FXML
    private void submitDetails() {
        this.card.setTitle(cardTitle.getText());
        this.card.setDescription(cardDescription.getText());
        this.closeModal();
        this.cardComponent.refresh();
    }

    @FXML
    private void deleteCard() {
        this.cardComponent.getColumnParent().deleteCard(this.cardComponent);
        this.cardComponent.getColumnParent().getColumn().removeCard(this.card);
        this.closeModal();
        this.cardComponent.getColumnParent().refresh();
    }

    /**
     * Refreshes the modal
     */
    public void refresh() {
        this.refreshDescription();
        this.refreshTitle();
        this.refreshTags();
        this.refreshSubtasks();
        this.refreshColorSchemes();
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
        tagsContainer.getChildren().clear();

        final Board currentBoard = boardService.getCurrentBoard();
        final Set<Tag> tags = currentBoard.getTags();

        for (final Tag tag : tags) {
            final TagComponent tagComponent = new TagComponent(boardService, parentScene, this, tag);
            tagsContainer.getChildren().add(tagComponent);
        }
    }

    /**
     * Refreshes subtasks in the scroll pane and displays them in their component form
     */
    public void refreshSubtasks() {
        // TODO
    }

    /**
     * Refreshes color schemes in the color scheme combo box
     */
    public void refreshColorSchemes() {
        this.colorSchemeComboBox.getItems().clear();
        final List<ColorScheme> colorSchemes = boardService.getCurrentBoard().getColorPresets();
        this.colorSchemeComboBox.getItems().addAll(colorSchemes);
    }

}
