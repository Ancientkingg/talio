package client.scenes;

import client.items.Column;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class CreateColumnCtrl {
    private final MainCtrl mainCtrl;

    @FXML
    private TextField columnName;

    @Inject
    public CreateColumnCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Will be used to create a column when user passes through the column name
     */
    public void createColumn() {
        Column column = new Column(columnName.getText());
        mainCtrl.addColumn(column);
        mainCtrl.closeSecondaryStage();
        mainCtrl.showOverview();
        mainCtrl.refreshOverview();
    }

    public void clearFields() {
        columnName.clear();
    }
}
