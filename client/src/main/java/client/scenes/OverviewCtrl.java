package client.scenes;

import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.*;

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
    public void openColumnInput() {
        mainCtrl.showCreateColumn();
    }

    /**
     * Refreshes the overview scene columnBox by iterating over each column in the current board
     * and displaying the corresponding titles. Will also refresh tasks in the future.
     *
     * Is this inefficient? Or does one have to reload all FXML objects to refresh?
     */
    public void refresh() {
        columnBox.getChildren().clear();
        for (final Column col : mainCtrl.getCurrentBoard().getColumns()) {
            final VBox taskBox = new VBox();
            taskBox.setStyle(
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-insets: 5;" +
                    "-fx-border-radius: 5;" +
                    "-fx-border-color: black;");
            taskBox.setPrefHeight(364);
            taskBox.setPrefWidth(157);
            columnBox.getChildren().add(taskBox);

            final TextArea columnTitle = new TextArea();
            columnTitle.setPrefHeight(27);
            columnTitle.setPrefWidth(153);
            columnTitle.setText(col.getHeading());
            columnTitle.setFont(new Font("System Bold", 12));
            taskBox.getChildren().add(columnTitle);
        }
    }
}
