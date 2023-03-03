package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.IOException;

public class JoinBoardCtrl {
    private final MainCtrl mainCtrl;

    private Stage stage;
    private Scene scene;

    @Inject
    public JoinBoardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Allows user to join a board upon inputting the board join code
     * @param e assigned to "join board" button
     * @throws IOException throws IO exception if FXML file not found
     */
    public void joinBoard(final ActionEvent e) throws IOException {
        mainCtrl.showOverview();
    }

    /**
     * Routes user to the "Create Board" stage where they can input a board name
     * @param e assigned to "Create Board" button
     * @throws IOException throws IO exception if FXML file not found
     */
    public void createBoard(final ActionEvent e) throws IOException {
        mainCtrl.showCreateBoard();
    }



}
