package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Demo {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label columnName;

    /**
     * Redirects user to "createColumn" FXML file on button press
     * where they can input a custom name for a new column to be created.
     * @throws IOException
     */
    public void openColumnInput(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("client/scenes/CreateColumn.fxml"));
        stage = new Stage(); // new stage to create pop-up
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void createColumn(){

    }


}
