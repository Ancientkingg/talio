package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.IOException;

public class CreateBoardCtrl {
    private final MainCtrl mainCtrl;

    private Stage stage; //variables declared with global scope for future controllers
    private Scene scene;
    private Parent root;

    @Inject
    public CreateBoardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Routes the user to the overview of the board they just created
     * upon action event e (create board button press)
     * @param e "create board" button on click
     * @throws IOException thrown if method encounters IO exception/ FXML file is not found
     */
    public void finalizeCreateBoard(final ActionEvent e) throws IOException {
        mainCtrl.showOverview();
    }
}
