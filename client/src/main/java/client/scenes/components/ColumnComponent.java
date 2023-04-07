package client.scenes.components;

import client.Main;
import client.exceptions.BoardChangeException;
import client.scenes.OverviewCtrl;
import client.services.BoardService;
import commons.Card;
import commons.Column;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;
import java.util.SortedSet;

public class ColumnComponent extends GridPane {

    private final BoardService boardService;
    private final Column column;

    private final Scene overviewScene;

    @FXML
    private TextField columnHeading;

    @FXML
    private Button deleteColumnButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox innerCardList;

    @FXML
    private Button addCardButton;

    /**
     * Constructor for ColumnComponent
     *
     * @param boardService   BoardService instance
     * @param column       Column instance
     * @param overviewCtrl OverviewCtrl instance
     * @param overviewScene overview scene
     */
    public ColumnComponent(final BoardService boardService, final Column column, final OverviewCtrl overviewCtrl, final Scene overviewScene) {
        this.boardService = boardService;
        this.column = column;

        this.overviewScene = overviewScene;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/Column.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Set the delete action for the delete column button
        deleteColumnButton.setOnAction(e -> {
            try {
                this.delete();
                overviewCtrl.refreshColumn();
            } catch (BoardChangeException ex) {
                throw new RuntimeException(ex);
            }
        });
        addHeadingChangeListener(boardService, column);
        // Set the add action for the add card button
        addCardButton.setOnAction(e -> {
            try {

                final SortedSet<Card> cards = column.getCards();

                final int priority = cards.size() == 0 ? 0 : cards.last().getPriority() + 1;

                boardService.addCardToColumn(new Card("", priority, "", null), column);
                this.refresh();
            } catch (BoardChangeException ex) {
                throw new RuntimeException(ex);
            }

        });

        setUpDragAndDrop(overviewCtrl);

        refresh();
    }

    private void addHeadingChangeListener(final BoardService boardService, final Column column) {
        columnHeading.focusedProperty().addListener((observable, oldValue, newValue) -> { // Save the heading of the column
            if (!newValue) boardService.renameColumn(column, columnHeading.getText()); // when focus is lost, rename column
        });

        columnHeading.setOnKeyReleased(keyEvent -> {
            if (Objects.equals(KeyCode.ENTER, (keyEvent.getCode()))) {
                boardService.renameColumn(column, columnHeading.getText());
                scrollPane.requestFocus();
            }
        });
    }

    private void setUpDragAndDrop(final OverviewCtrl overviewCtrl) {
        setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasString()) {
                // Allow dropping of cards
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        setOnDragDropped(event -> {
            final Dragboard db = event.getDragboard();
            if (db.hasString()) {
                final String[] split = db.getString().split(":");

                if (split.length == 2) {
                    final long cardId = Long.parseLong(split[0]);
                    final long columnIdx = Long.parseLong(split[1]);

                    final int priority = event.getPickResult().getIntersectedNode().getClass() == CardComponent.class ?
                            ((CardComponent) event.getPickResult().
                                    getIntersectedNode()).getCard().getPriority() : column.getCards().size() + 1;




                    boardService.repositionCard(cardId, columnIdx, column.getIndex(), priority);

                    overviewCtrl.refreshColumn(column.getIndex());
                    overviewCtrl.refreshColumn(columnIdx);
                }
            }
        });
    }

    /**
     * Sets the heading of the column
     *
     * @param heading String to set the heading to
     */
    public void setHeading(final String heading) {
        columnHeading.setText(heading);
    }

    /**
     * Deletes the column
     *
     * @throws BoardChangeException If the board cannot be changed
     */
    public void delete() throws BoardChangeException {
        boardService.removeColumnFromCurrentBoard(column);
    }

    /**
     * Deletes a card from the column
     *
     * @param card CardComponent instance
     */
    public void deleteCard(final CardComponent card) {
        innerCardList.getChildren().remove(card);
    }

    /**
     * Returns the column
     *
     * @return Column
     */
    public Column getColumn() {
        return column;
    }

    /**
     * Refreshes this component
     */
    public void refresh() {
        innerCardList.getChildren().clear();
        for (final Card card : column.getCards()) {
            final CardComponent cc = new CardComponent(boardService, card, this, overviewScene);
            innerCardList.getChildren().add(cc);
        }
        columnHeading.setText(column.getHeading());
//        innerCardList.getChildren().add(this.addCardButton);
    }
}
