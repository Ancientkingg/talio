package client.scenes;

import commons.*;
import javafx.fxml.FXML;
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
            columnBox.getChildren().add(taskBox);

            final Text columnTitle = new Text();
            columnTitle.setText(col.getHeading());
            taskBox.getChildren().add(columnTitle);
        }
    }
}
