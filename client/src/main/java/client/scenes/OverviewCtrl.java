package client.scenes;

import commons.Column;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import javax.inject.Inject;

public class OverviewCtrl {
    private final MainCtrl mainCtrl;

    @FXML
    private HBox columnBox;

    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     */
    @Inject
    public OverviewCtrl(final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
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
        for (final Column col : mainCtrl.getCurrentBoard().getColumns()) {
            final VBox cardBox = new VBox();
            cardBox.setStyle(
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-insets: 5;" +
                    "-fx-border-radius: 5;" +
                    "-fx-border-color: black;");
            cardBox.setPrefHeight(364);
            cardBox.setPrefWidth(157);
            columnBox.getChildren().add(cardBox);

            final TextArea columnTitle = new TextArea();
            columnTitle.setPrefHeight(27);
            columnTitle.setPrefWidth(153);
            columnTitle.setText(col.getHeading());
            columnTitle.setFont(new Font("System Bold", 12));
            cardBox.getChildren().add(columnTitle);

            final Button deleteColumn = new Button("Delete column");
            deleteColumn.setDefaultButton(true);
            deleteColumn.setOnAction(e -> {
                mainCtrl.getCurrentBoard().removeColumn(col);
                mainCtrl.refreshOverview();
            }); // Performed when button to delete a column is pressed
            deleteColumn.setPrefHeight(12);
            deleteColumn.setPrefWidth(149);
            cardBox.getChildren().add(deleteColumn);
        }
    }
}
