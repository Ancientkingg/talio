package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JoinBoard {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void joinBoard(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("client/scenes/Demo.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
