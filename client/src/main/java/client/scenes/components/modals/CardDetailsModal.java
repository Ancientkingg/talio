package client.scenes.components.modals;

import client.Main;
import client.scenes.LiveUIController;
import client.scenes.components.CardComponent;
import client.scenes.components.TagSelectComponent;
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
import java.util.Iterator;
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
        final List<Tag> selectedTags = this.getSelectedTags();

        for (final Tag tag : selectedTags) {
            if (!this.card.getTags().contains(tag)) {
                this.card.addTag(tag);
                boardService.addTagToCard(this.card, tag);
            }
        }

        Iterator it = this.card.getTags().iterator();

        while (it.hasNext()) {
            Tag tag = (Tag) it.next();
            if (!selectedTags.contains(tag)) {
                it.remove();
                boardService.removeTagFromCard(this.card, tag);
            }
        }

        boardService.editCard(this.card, this.cardComponent.getColumnParent().getColumn());

        this.closeModal();
        this.cardComponent.refresh();
    }

    /**
     * Deletes the card
     */
    @FXML
    private void deleteCard() {
        this.cardComponent.getColumnParent().deleteCard(this.cardComponent);
        this.cardComponent.getColumnParent().getColumn().removeCard(this.card);
        boardService.removeCardFromColumn(this.card, this.cardComponent.getColumnParent().getColumn());
        this.closeModal();
        this.cardComponent.getColumnParent().refresh();
    }

    @FXML
    private void onAddSubtaskButtonClick() {
        // TODO
    }

    private List<Tag> getSelectedTags() {
        return this.tagsContainer.getChildren().stream()
                .filter( child -> child instanceof TagSelectComponent)
                .map( child -> (TagSelectComponent) child)
                .filter( tagComponent -> tagComponent.isSelected())
                .map( tagComponent -> tagComponent.getTag()).toList();
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
            final TagSelectComponent tagComponent = new TagSelectComponent(boardService, parentScene, this, tag);
            if (card.getTags().contains(tag)) {
                tagComponent.setSelected(true);
            }
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
