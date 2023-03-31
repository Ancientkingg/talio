package client.scenes.components;

import client.Main;
import client.services.BoardService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class TagComponent {

    private final BoardService boardService;

    private final String title;

    @FXML
    private Text tagTitle;

    @FXML
    private VBox colorBubble;




    public TagComponent(final BoardService boardService, final String title) {
        this.boardService = boardService;
        this.title = title;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/Tag.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            tagTitle.textProperty().set(this.title);
            colorBubble.setStyle("-fx-background-color: " + this.colorGenerator());
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }


    }

    private String colorGenerator () {
        final String [] colors = {"#2196F3", "#92D36E", "#FF3823", "#92D36E", "#00FFFF",
                "#FF0000", "#ff9a00", "#694130", "#5A5A82" };

        return colors [(int) (Math.random() * colors.length)];
    }
}
