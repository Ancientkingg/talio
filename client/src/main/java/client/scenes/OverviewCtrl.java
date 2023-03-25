package client.scenes;

import client.models.BoardModel;
import client.scenes.components.CardComponent;
import client.scenes.components.ColumnComponent;
import commons.Column;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import javax.inject.Inject;

public class OverviewCtrl {
    private final MainCtrl mainCtrl;
    private final BoardModel boardModel;

    @FXML
    private HBox columnBox;

    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     * @param boardModel Shared instance of BoardModel
     */
    @Inject
    public OverviewCtrl(final MainCtrl mainCtrl, final BoardModel boardModel) {
        this.mainCtrl = mainCtrl;
        this.boardModel = boardModel;
    }


    /**
     * Redirects user to "createColumn" FXML file on button press
     * where they can input a custom name for a new column to be created.
     */
    public void showCreateColumn() {
        mainCtrl.showCreateColumn();
    }

    /**
     * Redirects user to "createCard" FXML file on button press
     * where they can create a card.
     */
    public void showCreateCard() {
        mainCtrl.showCreateCard();
    }

    /**
     * Refreshes the overview scene columnBox by iterating over each column in the current board
     * and displaying the corresponding titles. Will also refresh cards in the future.
     */
    public void refreshColumn() {
        columnBox.getChildren().clear();
        for (final Column col : boardModel.getCurrentBoard().getColumns()) {
            final ColumnComponent columnComponent = new ColumnComponent(boardModel, col, this);

            columnComponent.setHeading(col.getHeading());

            columnBox.getChildren().add(columnComponent);
        }
    }

    /**
     * Refreshes the component containing the given column
     * @param columnIdx index of the column to be found
     */
    public void refreshColumn(final long columnIdx) {
        for (final Node n : columnBox.getChildren()) {
            final ColumnComponent cc = (ColumnComponent) n;
            if (cc.getColumn().getIndex() == columnIdx) {
                cc.refresh();
                break;
            }
        }
    }

    /**
     * Refreshes the card with the given id
     * @param cardId id of the card to be found
     */
    public void refreshCard(final long cardId) {
        for (final Node n : columnBox.getChildren()) {
            final ColumnComponent cc = (ColumnComponent) n;
            for (final Node c : cc.getChildren()) {
                final CardComponent cac = (CardComponent) c;
                if (cac.getCard().getId() == cardId) {
                    cac.refresh();
                    break;
                }
            }
        }
    }
}
