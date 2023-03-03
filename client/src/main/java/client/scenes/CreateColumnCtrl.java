package client.scenes;

import commons.*;

import java.util.TreeSet;

import javafx.fxml.FXML;
import javax.inject.Inject;

import javafx.scene.control.TextField;


public class CreateColumnCtrl {
    private int demoIndexCounter = 0;

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
        final Column column = new Column(columnName.getText(), demoIndexCounter++, new TreeSet<>());
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
