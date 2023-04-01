package client.scenes.components;

import client.Main;
import client.services.BoardService;
import commons.ColorScheme;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class TagComponent extends GridPane {

    private final BoardService boardService;

    private final String title;

    private final ColorScheme colorScheme;

    @FXML
    private Text tagTitle;

    @FXML
    private VBox colorBubble;


    /**
     * Constructor for tag
     *
     * @param boardService boardService instance
     * @param title        title of the tag
     * @param colorScheme the colorScheme of the tag used for the color bubble
     */
    public TagComponent(final BoardService boardService, final String title, final ColorScheme colorScheme) {
        this.boardService = boardService;
        this.title = title;
        this.colorScheme = colorScheme;

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
            if (Objects.equals(colorScheme.getBackgroundColor(), new Color(0, 0, 0))) {
                colorBubble.setStyle("-fx-background-color: " + this.colorGenerator());
            } else {
                colorBubble.setStyle("-fx-background-color: " + colorScheme.getBackgroundColor().toString());
                //Not sure if functional, might have to mak custom toString method
            }
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Returns a random color for a tag
     * @return String containing hex code
     */
    String colorGenerator() {
        final String [] colors = {"#2196F3", "#92D36E", "#FF3823", "#92D36E",
                                  "#00FFFF", "#FF0000", "#ff9a00", "#694130", "#5A5A82" };

        return colors [(int) (Math.random() * colors.length)];
    }
}
