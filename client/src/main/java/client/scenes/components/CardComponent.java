package client.scenes.components;

import client.Main;
import client.exceptions.BoardChangeException;
import client.scenes.components.modals.CardDetailsModal;
import client.services.BoardService;
import commons.Card;
import commons.Tag;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CardComponent extends Draggable implements UIComponent {
    private final BoardService boardService;
    @Getter
    private final Card card;
    @Getter
    private final ColumnComponent columnParent;

    @FXML
    private TextArea cardText;

    @FXML
    private Button editCardButton;

    @FXML
    private HBox tagContainer;

    @FXML
    private Text descriptionIndicator;

    @FXML
    private Text subtaskCounter;

    private Node oldIntersectedComponent;

    /**
     * Constructor for CardComponent
     *
     * @param boardService   BoardService instance
     * @param card         Card instance
     * @param columnParent ColumnComponent instance
     */
    public CardComponent(final BoardService boardService, final Card card, final ColumnComponent columnParent) {
        super(columnParent.getOverviewCtrl(), columnParent);
        this.boardService = boardService;
        this.card = card;
        this.columnParent = columnParent;

        loadSource(Main.class.getResource("/components/Card.fxml"));

        cardText.setText(card.getTitle());
        cardText.setWrapText(true);


        setupDynamicallyResize();

        setDraggable(true);

        editCardButton.setOnAction(e -> cardText.setDisable(false)); // Temporarily enable editing of card text

        setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    final CardDetailsModal modal = new CardDetailsModal(boardService, getColumnParent().getScene(), getCard(), CardComponent.this);
                    modal.showModal();
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

//        setUpDragAndDrop();
        refresh();
    }

    protected CardComponent(final Card card, final ColumnComponent columnParent) {
        super(null, null);
        this.boardService = null;
        this.card = card;
        this.columnParent = columnParent;
        loadSource(Main.class.getResource("/components/Card.fxml"));
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
                boardService.editCard(card, columnParent.getColumn());
                cardText.setDisable(true);
            }
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
     * Gets called when the card is dropped on another component
     * @param intersectedComponent The component the card was dropped on
     * @param isBelow Whether the card was dropped below the component
     */
    protected void onDrop(final Node intersectedComponent, final boolean isBelow) {
        if (!(intersectedComponent instanceof CardComponent) && !(intersectedComponent instanceof ColumnComponent))
            throw new RuntimeException("Trying to drop a card on a non-card component");

        if (intersectedComponent instanceof final ColumnComponent intersectedColumn &&
                intersectedColumn.getColumn().getCards().size() == 0)
        {

            boardService.repositionCard(card.getId(), columnParent.getColumn().getIndex(),
                    intersectedColumn.getColumn().getIndex(), 0);

        } else if (intersectedComponent instanceof final CardComponent intersectedCardComponent) {

            final Card intersectedCard = intersectedCardComponent.getCard();

            int priority = intersectedCard.getPriority();
            if (isBelow) priority++;

            boardService.repositionCard(card.getId(), columnParent.getColumn().getIndex(),
                    intersectedCardComponent.getColumnParent().getColumn().getIndex(), priority);
        }
    }

    protected void duringDrag(final Node intersectedComponent, final boolean isBelow) {

        if (!intersectedComponent.isVisible() || oldIntersectedComponent == intersectedComponent) return;
        oldIntersectedComponent = intersectedComponent;

        if (intersectedComponent instanceof final CardComponent intersectedCardComponent) {

            final CardComponent cardDropIndicator = new CardComponent(intersectedCardComponent.getCard(), intersectedCardComponent.getColumnParent());

            cardDropIndicator.setVisible(false);

            // Selecting a card
            final ColumnComponent intersectedColumn = intersectedCardComponent.getColumnParent();
            int index = intersectedColumn.getInnerCardList().getChildren().indexOf(intersectedCardComponent);
            if (isBelow) index++;

            if (!intersectedColumn.getInnerCardList().getChildren().contains(cardDropIndicator))
                intersectedColumn.getInnerCardList().getChildren().add(index, cardDropIndicator);

            final Timeline timeline = new Timeline();
            final KeyFrame k1 = new KeyFrame(Duration.millis(100), e -> {
                if (oldIntersectedComponent != intersectedComponent) {
                    intersectedColumn.getInnerCardList().getChildren()
                        .removeIf(c ->
                            c instanceof CardComponent &&
                                ((CardComponent) c).boardService == null);
                } else {
                    timeline.playFromStart();
                }
            });
            timeline.getKeyFrames().add(k1);
            Platform.runLater(timeline::play);
        }
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

        if (card.getDescription().equals("") || card.getDescription() == null) {
            descriptionIndicator.setVisible(false);
        } else {
            descriptionIndicator.setVisible(true);
        }

        if (card.getSubtasks().size() != 0) {
            subtaskCounter.setText(card.countFinishedSubtasks() + "/" + card.getSubtasks().size());
            subtaskCounter.setVisible(true);
        } else {
            subtaskCounter.setVisible(false);
        }

    }

    /**
     * Clones the card
     * @return The cloned card
     */
    public Draggable clone() {
        return new CardComponent(boardService, card, columnParent);
    }
}
