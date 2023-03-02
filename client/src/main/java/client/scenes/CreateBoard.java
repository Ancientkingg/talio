package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateBoard {

    private Stage stage; //variables declared with global scope for future controllers
    private Scene scene;
    private Parent root;

    /**
     * Routes the user to the overview of the board they just created
     * upon action event e (create board button press)
     * @param e "create board" button on click
     * @throws IOException thrown if method encounters IO exception/ FXML file is not found
     */
    public void finalizeCreateBoard(final ActionEvent e) throws IOException {
        final Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("client/scenes/Overview.fxml"));
        final Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow(); // Window is not changed, so current stage is passed through
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
