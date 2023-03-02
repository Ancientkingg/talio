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

    /**
     * Allows user to join a board upon inputting the board join code
     * @param e assigned to "join board" button
     * @throws IOException throws IO exception if FXML file not found
     */
    public void joinBoard(final ActionEvent e) throws IOException {
        final Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("client/scenes/Overview.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Routes user to the "Create Board" stage where they can input a board name
     * @param e assigned to "Create Board" button
     * @throws IOException throws IO exception if FXML file not found
     */
    public void createBoard(final ActionEvent e) throws IOException {
        final Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("client/scenes/CreateBoard.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}
