package client.scenes.components;

import client.Main;
import client.exceptions.BoardChangeException;
import client.scenes.components.modals.CardDetailsModal;
import client.services.BoardService;
import commons.Card;
import commons.Tag;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CardComponent extends GridPane implements UIComponent {
    private final BoardService boardService;
    @Getter
    private final Card card;
    @Getter
    private final ColumnComponent columnParent;
    private final Scene overviewScene;

    @FXML
    private TextArea cardText;

    @FXML
    private Button editCardButton;

    @FXML
    private HBox tagContainer;

    /**
     * Constructor for CardComponent
     *
     * @param boardService   BoardService instance
     * @param card         Card instance
     * @param columnParent ColumnComponent instance
     * @param overviewScene overview scene
     */
    public CardComponent(final BoardService boardService, final Card card, final ColumnComponent columnParent, final Scene overviewScene) {
        this.boardService = boardService;
        this.card = card;
        this.columnParent = columnParent;

        this.overviewScene = overviewScene;

        loadSource(Main.class.getResource("/components/Card.fxml"));

        cardText.setText(card.getTitle());
        cardText.setWrapText(true);

        setupDynamicallyResize();

        editCardButton.setOnAction(e -> cardText.setDisable(false)); // Temporarily enable editing of card text

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        final CardDetailsModal modal = new CardDetailsModal(boardService, getColumnParent().getScene(), getCard(), CardComponent.this);
                        modal.showModal();
                    }
                }
                }
            });

        cardText.setOnKeyReleased(e -> { // Disable editing of card text when enter is pressed
            if (e.getCode() == KeyCode.ENTER) {
                cardText.setDisable(true);
                card.setTitle(cardText.getText());
                refresh();
            }
        });

        this.listenForTitleChanges();


        cardText.setDisable(true); // Disable editing of card text by default

        setUpDragAndDrop();
        setHover();
        refresh();
    }

    private void setHover() {
        this.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                columnParent.getOverviewCtrl().setFocussedCard(this);
            }
        });
    }

    /**
     * Change editability of card
     * @param setDisable value to be passed to method setDisable
     */
    public void toggleCardEditing (final boolean setDisable) {
        editCardButton.setDisable(setDisable);
    }

    /**
     * Sets up dynamically resizing of the card component
     */
    private void setupDynamicallyResize() {
        cardText.sceneProperty().addListener((observableNewScene, oldScene, newScene) -> {
            if (newScene != null) {
                applyCss();
                final Node text = lookup(".text");

                // 2)
                prefHeightProperty().bind(Bindings.createDoubleBinding(() -> {
                    return cardText.getFont().getSize() + text.getBoundsInLocal().getHeight() + 10;
                }, text.boundsInLocalProperty()));

                // 1)
                text.boundsInLocalProperty().addListener((observableBoundsAfter, boundsBefore, boundsAfter) -> {
                    Platform.runLater(() -> requestLayout());
                });
            }
        });
    }

    /**
     * Listens for changes in title
     */
    public void listenForTitleChanges () {
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                card.setTitle(cardText.getText());
                refresh();
                cardText.setDisable(true);
            }
        });

        cardText.textProperty().addListener((observable, oldValue, newValue) -> {
            card.setTitle(cardText.getText());
            refresh();
        });
    }


    private void setUpDragAndDrop() {
        setOnDragDetected(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Start drag-and-drop gesture
                startFullDrag();
                final Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                final ClipboardContent content = new ClipboardContent();
                content.putString(
                        String.format("%s:%s", card.getId(), columnParent.getColumn().getIndex()));
                dragboard.setContent(content);
                event.consume();
            }
        });

        setOnDragEntered(event -> {
            // Check if the dragged element is being dragged over the top half of the target element
            if (event.getY() < this.getHeight()) {
                this.setStyle("-fx-border-color: transparent; -fx-border-width: 25px 0 0 0;");
            }
        });

        setOnDragExited(event -> {
            // Remove the top margin from the target element
            this.setStyle("-fx-border-color: transparent; -fx-border-width: 0;");
        });
    }

    /**
     * Deletes the card
     *
     * @throws BoardChangeException If the card could not be deleted
     */
    public void delete() throws BoardChangeException {
        boardService.removeCardFromColumn(card, columnParent.getColumn());
    }

    /**
     * Refreshes the card - to be called when updating the interface
     */
    public void refresh() {
        cardText.setText(card.getTitle());
        tagContainer.getChildren().clear();
        tagContainer.setSpacing(2);
        if (card.getTags().size() < 5) {
            for (final Tag tag : card.getTags()) {
                tagContainer.getChildren().add(new OverviewTagComponent(boardService, tag));
            }
        } else {
            final List<Tag> tags = new ArrayList<>(card.getTags());
            for (int i = 0; i < 4; i++) {
                final OverviewTagComponent otc = new OverviewTagComponent(boardService, tags.get(i));
                otc.setPrefWidth(40);
                tagContainer.getChildren().add(otc);
            }
            final Label moreTags = new Label(String.format("+%s more", card.getTags().size() - 4));
            moreTags.setFont(javafx.scene.text.Font.font("fonts/Cabin-Regular.ttf", 9));
            moreTags.setStyle("-fx-text-fill: #4f4f4f;-fx-padding: 0 0 15px 0;");
            tagContainer.getChildren().add(moreTags);
        }
    }
}
