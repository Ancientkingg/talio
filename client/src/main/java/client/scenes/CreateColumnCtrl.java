package client.scenes;

import client.exceptions.BoardChangeException;
import commons.*;

import java.util.TreeSet;

import javafx.fxml.FXML;
import javax.inject.Inject;

import javafx.scene.control.TextField;


public class CreateColumnCtrl {

    private final MainCtrl mainCtrl;

    @FXML
    private TextField columnName;
    private int demoIndexCounter; //This is just a temporary fix to give columns different indexes


    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     */
    @Inject
    public CreateColumnCtrl(final MainCtrl mainCtrl) {
        this.demoIndexCounter = 1;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Will be used to create a column when user passes through the column name
     */
    public void createColumn() throws BoardChangeException {
        final Column column = new Column(columnName.getText(), demoIndexCounter++, new TreeSet<>());
        mainCtrl.addColumn(column);
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
