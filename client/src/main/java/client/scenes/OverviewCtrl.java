package client.scenes;

import client.scenes.components.ColumnComponent;
import client.services.BoardService;
import commons.Column;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

import javax.inject.Inject;

public class OverviewCtrl {
    private final MainCtrl mainCtrl;
    private final BoardService boardService;

    @FXML
    private HBox columnBox;

    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     * @param boardService Shared instance of BoardService
     */
    @Inject
    public OverviewCtrl(final MainCtrl mainCtrl, final BoardService boardService) {
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
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
     * -
     * Is this inefficient? Or does one have to reload all FXML objects to refresh?
     */
    public void refresh() {
        columnBox.getChildren().clear();
        for (final Column col : boardService.getCurrentBoard().getColumns()) {
            final ColumnComponent columnComponent = new ColumnComponent(boardService, col, this);

            columnComponent.setHeading(col.getHeading());

            columnBox.getChildren().add(columnComponent);
        }
    }
}
