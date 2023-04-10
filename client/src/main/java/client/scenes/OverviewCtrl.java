package client.scenes;

import client.Main;
import client.exceptions.BoardChangeException;
import client.scenes.components.CardComponent;
import client.scenes.components.ColumnComponent;
import client.scenes.components.Draggable;
import client.scenes.components.modals.BoardPasswordModal;
import client.scenes.components.modals.BoardSettingsModal;
import client.scenes.components.modals.ColorPresetsOverviewModal;
import client.scenes.components.modals.TagsOverviewModal;
import client.services.BoardService;
import commons.ColorScheme;
import commons.Column;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.awt.datatransfer.StringSelection;
import java.util.TreeSet;

public class OverviewCtrl implements Refreshable {
    private final MainCtrl mainCtrl;
    private final BoardService boardService;

    @FXML
    private HBox columnBox;
    @FXML
    private Button linkButton;

    @FXML
    private SVGPath lockIcon;

    @Setter
    @Getter
    private boolean isLocked;
    
    @FXML
    private Button boardNameButton;

    @FXML
    private SVGPath boardEditIcon;

    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     * @param boardService Shared instance of BoardService
     */
    @Inject
    public OverviewCtrl(final MainCtrl mainCtrl, final BoardService boardService) {
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
        this.isLocked = true;
    }

    /**
     * Initialize overview scene
     */
    @FXML
    public void initialize() {
        setHover();
    }

    private void setHover() {
        this.boardNameButton.hoverProperty().addListener((obs, newVal, oldVal) -> {
            if (newVal) {
                this.boardEditIcon.setStyle("-fx-fill: transparent");
            } else {
                this.boardEditIcon.setStyle("-fx-fill: #f3f3f3");
            }
        });
    }

    /**
     * Refreshes the overview
     */
    public void refresh() {
        this.boardNameButton.setText(boardService.getCurrentBoard().getTitle());
        ((StackPane) mainCtrl.getCurrentScene().getRoot()).getChildren()
            .removeIf(c -> c instanceof Draggable);
        this.refreshColumn();
        this.refreshStyle();
        this.refreshLock();
    }

    /**
     * Refreshes the overview scene columnBox by iterating over each column in the current board
     * and displaying the corresponding titles. Will also refresh cards in the future.
     */
    public void refreshColumn() {
        columnBox.getChildren().removeAll(columnBox.getChildren().stream().filter(c -> c instanceof ColumnComponent).toList());

        for (final Column col : boardService.getCurrentBoard().getColumns()) {
            final ColumnComponent columnComponent = new ColumnComponent(boardService, col, this, mainCtrl);

            columnComponent.setHeading(col.getHeading());

            columnBox.getChildren().add(columnBox.getChildren().size() - 1,columnComponent);
        }
    }

    private void refreshStyle() {
        final ColorScheme defaultColorScheme = boardService.getCurrentBoard().getBoardColorScheme();

        columnBox.setStyle("-fx-background-color: " + defaultColorScheme.getBackgroundColor() + ";");

        this.boardNameButton.setStyle("-fx-text-fill: " + defaultColorScheme.getTextColor() + ";");
    }

    /**
     * Refreshes the component containing the given column
     * @param columnId index of the column to be found
     */
    public void refreshColumn(final long columnId) {
        for (final Node n : columnBox.getChildren()) {
            final ColumnComponent cc = (ColumnComponent) n;
            if (cc.getColumn().getId() == columnId) {
                cc.refresh();
                break;
            }
        }
    }

    /**
     * Refreshes the heading of the component containing the given column
     * @param columnId index of the column to be found
     */
    public void refreshColumnHeading(final long columnId) {
        for (final Node n : columnBox.getChildren()) {
            final ColumnComponent cc = (ColumnComponent) n;
            if (cc.getColumn().getId() == columnId) {
                cc.setHeading(cc.getColumn().getHeading());
                break;
            }
        }
    }

    /**
     * Refreshes the card with the given id
     * @param cardId id of the card to be found
     */
    public void refreshCard(final long cardId) {
        for (final Node n : columnBox.getChildren()) {
            if (!(n instanceof ColumnComponent)) continue;
            final ColumnComponent cc = (ColumnComponent) n;
            for (final Node c : cc.getInnerCardList().getChildren()) {
                final CardComponent cac = (CardComponent) c;
                if (cac.getCard().getId() == cardId) {
                    cac.refresh();
                    break;
                }
            }
        }
    }

    /**
     * Will be used to create a column when user passes through the column name
     */
    public void createColumn() throws BoardChangeException {
        final Column column = new Column(getFunColumnName(), boardService.getHighestIndex(), new TreeSet<>());
        column.generateId();
        boardService.addColumnToCurrentBoard(column);
        mainCtrl.refreshOverview();
    }

    /**
     * Handles the tags button click
     */
    @FXML
    public void onTagsButtonClick() {
        final TagsOverviewModal modal = new TagsOverviewModal(boardService, this.mainCtrl.getCurrentScene());
        mainCtrl.setTagsOverviewModal(modal);
        modal.showModal();
    }

    /**
     * Handles the colors button click
     */
    @FXML
    public void onColorsButtonClick() {
        final ColorPresetsOverviewModal modal = new ColorPresetsOverviewModal(boardService, this.mainCtrl.getCurrentScene());
        mainCtrl.setColorPresetsOverviewModal(modal);
        modal.showModal();
    }

    /**
     * Handles the lock button click
     */
    @FXML
    public void onLockButtonClick() {
        final BoardPasswordModal modal = new BoardPasswordModal(boardService, this.mainCtrl.getCurrentScene(), this);
        mainCtrl.setBoardPasswordModal(modal);
        modal.showModal();
    }

    /**
     * Checks the lock status of the current board
     */
    public void checkLock() {
        final String password = boardService.getCurrentBoard().getPassword();
        if (password == null || password.isEmpty()) {
            this.isLocked = false;
        } else {
            this.isLocked = true;
        }

        if (Main.isAdmin()) this.isLocked = false;
    }

    private void refreshLock() {
        this.setLockIcon(this.isLocked);
    }

    private void setLockIcon(final boolean lock) {
        if (lock) {
            // Closed
            this.lockIcon.setContent(
                    "M220 976q-24.75 0-42.375-17.625T160 916V482q0-24.75 17.625-42.375T220 " +
                            "422h70v-96q0-78.85 55.606-134.425Q401.212 136 480.106 136T614.5 191.575Q670 247.15 670" +
                            " 326v96h70q24.75 0 42.375 17.625T800 482v434q0 24.75-17.625 42.375T740 " +
                            "976H220Zm0-60h520V482H220v434Zm260.168-140Q512 776 534.5 753.969T557 701q0-30-22.668-54.5t-54.5-24.5Q448 " +
                            "622 425.5 646.5t-22.5 55q0 30.5 22.668 52.5t54.5 22ZM350 " +
                            "422h260v-96q0-54.167-37.882-92.083-37.883-37.917-92-37.917Q426 196 388 233.917 350 271.833 350 326v96ZM220 916V482v434Z"
            );
        } else {
            // Open
            this.lockIcon.setContent(
                    "M220 422h390v-96q0-54.167-37.882-92.083-37.883-37.917-92-37.917Q426" +
                            " 196 388 233.917 350 271.833 350 326h-60q0-79 55.606-134.5t134.5-55.5Q559 136 614.5" +
                            " 191.575T670 326v96h70q24.75 0 42.375 17.625T800 482v434q0 24.75-17.625 " +
                            "42.375T740 976H220q-24.75 0-42.375-17.625T160 916V482q0-24.75 17.625-42.375T220 422Zm0 " +
                            "494h520V482H220v434Zm260.168-140Q512 776 534.5 753.969T557 701q0-30-22.668-54.5t-54.5-24.5Q448 622 425.5 " +
                            "646.5t-22.5 55q0 30.5 22.668 52.5t54.5 22ZM220 916V482v434Z"
            );
        }
    }


    /**
     * Handles the link button click
     */
    @FXML
    public void onLinkButtonClick() {
        final Point2D p = linkButton.localToScreen(-110, 32);

        final Tooltip customTooltip = new Tooltip("Copied join-key to clipboard!");
        customTooltip.setAutoHide(false);
        customTooltip.show(linkButton,p.getX(),p.getY());

        final PauseTransition pt = new PauseTransition(Duration.millis(1250));
        pt.setOnFinished(e -> {
            customTooltip.hide();
        });
        pt.play();

        final StringSelection joinKeySelection = new StringSelection(boardService.getCurrentBoard().getJoinKey());
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(joinKeySelection, null);
    }

    @FXML
    private void onBoardEditButtonClick() {
        final BoardSettingsModal modal = new BoardSettingsModal(boardService, this.mainCtrl.getCurrentScene(), this);
        mainCtrl.setBoardSettingsModal(modal);
        modal.showModal();
    }

    /**
     * Handles the back button click
     */
    @FXML
    public void onBackButtonClick() {
        mainCtrl.showHomePage();
    }

    /**
     * Handles the settings button click
     */
    @FXML
    public void onSettingsButtonClick() {

    }

    /**
     * Generates a random column name
     * @return a fun column name
     */
    private String getFunColumnName() {
        final String[] adjectives = { "fun", "cool", "awesome", "great", "amazing", "wonderful", "fantastic",
                                        "incredible", "magnificent", "marvelous", "spectacular", "superb",
                                        "super", "fabulous", "fab", "excellent", "excellent", "terrific", "terrific",
                                        "wicked", "wicked"};

        final String[] animalNames = {"dog", "cat", "fish", "bird", "hamster", "rabbit", "turtle", "snake", "lizard",
                                      "frog", "toad", "salamander", "chameleon", "gecko", "iguana", "alligator",
                                      "crocodile", "lizard", "snake", "turtle", "tortoise", "shark", "whale",
                                      "dolphin", "seal", "otter", "bear", "panda", "monkey", "gorilla", "ape",
                                      "chimpanzee", "orangutan", "gibbon", "lemur", "squirrel", "chipmunk",
                                      "mouse", "rat", "rabbit", "hare", "deer", "elk", "moose", "buffalo", "cow",
                                      "bull", "horse", "pony", "zebra", "giraffe", "rhinoceros", "hippopotamus",
                                      "elephant", "camel", "llama", "alpaca", "sheep", "goat", "pig", "boar",
                                      "wolf", "fox", "coyote", "dog", "cat", "lion", "tiger", "leopard", "cheetah",
                                      "jaguar", "panther", "hyena", "bear", "panda", "koala", "kangaroo", "wallaby",
                                      "wombat", "opossum", "platypus", "kookaburra", "emu"};

        return adjectives[(int) (Math.random() * adjectives.length)]
                + " " + animalNames[(int) (Math.random() * animalNames.length)];
    }

}
