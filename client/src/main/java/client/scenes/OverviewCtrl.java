package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.IOException;

public class OverviewCtrl {
    private final MainCtrl mainCtrl;

    private Stage stage; //Variables declared globally for future controllers
    private Scene scene;

    @Inject
    public OverviewCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }


    /**
     * Redirects user to "createColumn" FXML file on button press
     * where they can input a custom name for a new column to be created.
     * @param e assigned to "create column" button
     * @throws IOException throws IO exception if FXML file not found or other IO mismatch
     */
    public void openColumnInput(final ActionEvent e) throws IOException {
        mainCtrl.showCreateColumn();
    }



}
