package client.scenes.components.modals;

import client.Main;
import client.scenes.OverviewCtrl;
import client.scenes.Refreshable;
import client.scenes.components.CardComponent;
import client.scenes.components.Draggable;
import client.scenes.components.SubTaskComponent;
import client.scenes.components.TagSelectComponent;
import client.services.BoardService;
import commons.*;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class CardDetailsModal extends Modal implements Refreshable {

    private final Card card;

    private final CardComponent cardComponent;

    private boolean isCardTitleChanged, isCardDescriptionChanged;

    @FXML
    private TextField cardTitle;

    @FXML
    private TextArea cardDescription;

    @FXML
    private VBox tagsContainer;

    @FXML
    private Button submitButton;

    @FXML @Getter
    private VBox subTasksContainer;

    @FXML
    private Label noTagsText;

    @FXML
    private Label addSubtasksText;

    @FXML
    private ComboBox<ColorScheme> colorSchemeComboBox;

    @FXML
    private Button addSubtaskButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Text deleteText;

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

        isCardTitleChanged = false;
        isCardDescriptionChanged = false;

        this.cardTitle.setText(card.getTitle());
        this.cardDescription.setText(card.getDescription());

        this.cardTitle.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue)
                isCardTitleChanged = true;
        });
        this.cardDescription.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue)
                isCardDescriptionChanged = true;
        });
    }

    /**
     * Submits data and refreshes overview when submit button is pressed;
     */
    @FXML
    private void submitDetails() {
        if (!OverviewCtrl.isLocked()) {
            this.card.setTitle(cardTitle.getText());
            this.card.setDescription(cardDescription.getText());
            final List<Tag> selectedTags = this.getSelectedTags();

            for (final Tag tag : selectedTags) {
                if (!this.card.getTags().contains(tag)) {
                    this.card.addTag(tag);
                    boardService.addTagToCard(this.card, tag);
                }
            }

            final Iterator it = this.card.getTags().iterator();
            while (it.hasNext()) {
                final Tag tag = (Tag) it.next();
                if (!selectedTags.contains(tag)) {
                    it.remove();
                    boardService.removeTagFromCard(this.card, tag);
                }
            }

            this.card.setColorScheme(this.colorSchemeComboBox.getValue());

            boardService.editCard(this.card, this.cardComponent.getColumnParent().getColumn());
        }

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
        boardService.addSubTask(this.card, "Edit me!");
        this.refreshSubtasks();
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
        this.refreshLock();
    }

    private void refreshLock() {
        if (OverviewCtrl.isLocked()) {
            cardTitle.setDisable(true);
            cardDescription.setDisable(true);
            colorSchemeComboBox.setDisable(true);
            addSubtaskButton.setDisable(true);
            deleteButton.setDisable(true);
            submitButton.setDisable(true);

            deleteButton.setVisible(false);
            deleteText.setVisible(false);
            addSubtaskButton.setVisible(false);
        } else {
            cardTitle.setDisable(false);
            cardDescription.setDisable(false);
            colorSchemeComboBox.setDisable(false);
            addSubtaskButton.setDisable(false);
            deleteButton.setDisable(false);
            submitButton.setDisable(false);

            deleteButton.setVisible(true);
            deleteText.setVisible(true);
            addSubtaskButton.setVisible(true);
        }
    }



    /**
     * Refresh description textArea
     */
    public void refreshDescription() {
        if (!isCardDescriptionChanged) cardDescription.setText(card.getDescription());
    }

    /**
     * Refresh title textArea
     */
    public void refreshTitle() { if (!isCardTitleChanged) cardTitle.setText(card.getTitle()); }

    /**
     * Refreshes tags in the scroll pane and displays them in their component form
     */
    public void refreshTags() {
        tagsContainer.getChildren().clear();

        final Board currentBoard = boardService.getCurrentBoard();
        final Set<Tag> tags = currentBoard.getTags();

        if (tags.isEmpty()) {
            tagsContainer.getChildren().add(noTagsText);
            return;
        }

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
        subTasksContainer.getChildren().clear();

        final SortedSet<SubTask> subtasks = card.getSubtasks();

        if (subtasks.isEmpty()) {
            subTasksContainer.getChildren().add(addSubtasksText);
            subTasksContainer.getChildren().add(addSubtaskButton);
            return;
        }

        for (final SubTask subtask : subtasks) {
            final SubTaskComponent subTaskComponent = new SubTaskComponent(subtask, card, boardService,
                    (e) -> refreshSubtasks(), this);
            subTasksContainer.getChildren().add(subTaskComponent);
        }

        ((StackPane) parentScene.getRoot()).getChildren()
                .removeIf(c -> c instanceof Draggable);

        subTasksContainer.getChildren().add(addSubtaskButton);
    }

    /**
     * Refreshes color schemes in the color scheme combo box
     */
    public void refreshColorSchemes() {
        this.colorSchemeComboBox.getItems().clear();
        final Set<ColorScheme> colorSchemes = boardService.getCurrentBoard().getColorPresets();
        this.colorSchemeComboBox.getItems().addAll(colorSchemes);
    }
}
