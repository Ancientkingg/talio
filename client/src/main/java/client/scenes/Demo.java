package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Demo {
    private Stage stage; //Variables declared globally for future controllers
    private Scene scene;



    /**
     * Redirects user to "createColumn" FXML file on button press
     * where they can input a custom name for a new column to be created.
     * @param e assigned to "create column" button
     * @throws IOException throws IO exception if FXML file not found or other IO mismatch
     */
    public void openColumnInput(final ActionEvent e) throws IOException {
        final Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("client/scenes/CreateColumn.fxml"));
        stage = new Stage(); // new stage to create pop-up
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}
