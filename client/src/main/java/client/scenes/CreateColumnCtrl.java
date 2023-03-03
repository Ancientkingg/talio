package client.scenes;

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
    public void enterColumnName() {
            //Functionality To be implemented
    }
}
