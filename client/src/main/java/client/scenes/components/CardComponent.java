package client.scenes.components;

import client.Main;
import client.exceptions.BoardChangeException;
import client.services.BoardService;
import commons.Card;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import lombok.Getter;

import java.io.IOException;

public class CardComponent extends GridPane {
    private final BoardService boardService;
    @Getter
    private final Card card;
    @Getter
    private final ColumnComponent columnParent;

    @FXML
    private TextArea cardText;

    @FXML
    private Button editCardButton;

    /**
     * Constructor for CardComponent
     *
     * @param boardService   BoardService instance
     * @param card         Card instance
     * @param columnParent ColumnComponent instance
     */
    public CardComponent(final BoardService boardService, final Card card, final ColumnComponent columnParent) {
        this.boardService = boardService;
        this.card = card;
        this.columnParent = columnParent;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/Card.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
    }
}
