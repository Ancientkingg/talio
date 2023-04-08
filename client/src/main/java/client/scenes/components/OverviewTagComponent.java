package client.scenes.components;

import client.Main;
import client.services.BoardService;
import commons.Color;
import commons.ColorScheme;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class OverviewTagComponent extends GridPane implements UIComponent {

    private final BoardService boardService;

    private final String title;

    private final ColorScheme colorScheme;

    @FXML
    private Pane tagBackground;


    /**
     * Constructor for OverviewTagComponent
     *
     * @param boardService boardService instance
     * @param tag          tag instance
     */
    public OverviewTagComponent(final BoardService boardService, final Tag tag) {
        this(boardService, tag.getTitle(), tag.getColorScheme());
    }

    /**
     * Constructor for OverviewTagComponent
     *
     * @param boardService boardService instance
     * @param title        title of the tag
     * @param colorScheme  the colorScheme of the tag used for the color bubble
     */
    public OverviewTagComponent(final BoardService boardService, final String title, final ColorScheme colorScheme) {
        this.boardService = boardService;
        this.title = title;
        this.colorScheme = colorScheme;

        loadSource(Main.class.getResource("/components/OverviewTag.fxml"));

        this.initialize();
    }

    private void initialize() {
        final Tooltip tt = new Tooltip(title);
        tt.setAutoHide(false);
        tt.setFont(javafx.scene.text.Font.font("System", 12));
        Tooltip.install(this, tt);

        // Set background color to a random color if it's not set yet
        if (colorScheme.getBackgroundColor().equals(new Color(0, 0, 0, 0)))
            colorScheme.setBackgroundColor(new Color(colorGenerator()));

        tagBackground.setStyle("-fx-background-color: " + colorScheme.getBackgroundColor());
    }

    /**
     * Returns a random color for a tag
     *
     * @return String containing hex code
     */
    String colorGenerator() {
        final String[] colors = {"#2196F3", "#92D36E", "#FF3823", "#92D36E", "#00FFFF", "#FF0000", "#ff9a00", "#694130", "#5A5A82"};

        return colors[(int) (Math.random() * colors.length)];
    }
}
