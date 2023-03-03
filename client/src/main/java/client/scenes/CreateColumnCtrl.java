package client.scenes;

import client.items.Column;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class CreateColumnCtrl {
    private final MainCtrl mainCtrl;

    @FXML
    private TextField columnName;

    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     */
    @Inject
    public CreateColumnCtrl(final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Will be used to create a column when user passes through the column name
     */
    public void createColumn() {
        final Column column = new Column(columnName.getText());
        mainCtrl.addColumn(column);
        mainCtrl.closeSecondaryStage();
        mainCtrl.showOverview();
        mainCtrl.refreshOverview();
    }

    /**
     * Clears fields to avoid accidental repetition of prior arguments
     */
    public void clearFields() {
        columnName.clear();
    }
}
