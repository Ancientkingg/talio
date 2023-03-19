package client.scenes.components;

import client.Main;
import client.exceptions.BoardChangeException;
import client.models.BoardModel;
import client.scenes.OverviewCtrl;
import commons.Card;
import commons.Column;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;

public class ColumnComponent extends GridPane {

    private final BoardModel boardModel;
    private final Column column;

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
     * @param boardModel BoardModel instance
     * @param column Column instance
     * @param overviewCtrl OverviewCtrl instance
     */
    public ColumnComponent(final BoardModel boardModel, final Column column, final OverviewCtrl overviewCtrl) {
        this.boardModel = boardModel;
        this.column = column;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/client/scenes/components/Column.fxml"));
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
                overviewCtrl.refresh();
            } catch (BoardChangeException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Set the add action for the add card button
        addCardButton.setOnAction(e -> {
            try {
                final long id = (long) (Math.random() * Long.MAX_VALUE);

                final SortedSet<Card> cards = column.getCards();

                final int priority = cards.size() == 0 ? 0 : cards.last().getPriority() + 1;

                boardModel.addCard(new Card(id, "", priority, "", null), column);
                overviewCtrl.refresh();
            } catch (BoardChangeException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Make the column unable to scroll horizontally
        scrollPane.setFitToWidth(true);

        for (Card card : column.getCards()) {
            innerCardList.getChildren().add(new CardComponent(boardModel, card, this));
        }
    }

    /**
     * Sets the heading of the column
     * @param heading String to set the heading to
     */
    public void setHeading(final String heading) {
        columnHeading.setText(heading);
    }

    /**
     * Deletes the column
     * @throws BoardChangeException If the board cannot be changed
     */
    public void delete() throws BoardChangeException {
        boardModel.removeColumn(column);
    }

    public void deleteCard(CardComponent card) {
        innerCardList.getChildren().remove(card);
    }

    /**
     * Returns the column
     * @return Column
     */
    public Column getColumn() {
        return column;
    }
}
