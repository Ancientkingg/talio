package client.scenes;

import client.exceptions.BoardChangeException;
import client.scenes.components.CardComponent;
import client.scenes.components.ColumnComponent;
import client.services.BoardService;
import commons.Column;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import javax.inject.Inject;
import java.awt.datatransfer.StringSelection;
import java.util.TreeSet;

public class OverviewCtrl {
    private final MainCtrl mainCtrl;
    private final BoardService boardService;

    @FXML
    private HBox columnBox;
    @FXML
    private Button linkButton;

    private static int highestIndex = 0; //This is just a temporary fix to give columns different indexes


    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     * @param boardService Shared instance of BoardService
     */
    @Inject
    public OverviewCtrl(final MainCtrl mainCtrl, final BoardService boardService) {
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
    }


    /**
     * Redirects user to "createCard" FXML file on button press
     * where they can create a card.
     */
    public void showCreateCard() {
        mainCtrl.showCreateCard();
    }

    /**
     * Refreshes the overview scene columnBox by iterating over each column in the current board
     * and displaying the corresponding titles. Will also refresh cards in the future.
     */
    public void refreshColumn() {
        columnBox.getChildren().removeAll(columnBox.getChildren().stream().filter(c -> c instanceof ColumnComponent).toList());

        for (final Column col : boardService.getCurrentBoard().getColumns()) {
            final ColumnComponent columnComponent = new ColumnComponent(boardService, col, this);

            columnComponent.setHeading(col.getHeading());

            columnBox.getChildren().add(columnBox.getChildren().size() - 1,columnComponent);
        }
    }

    /**
     * Refreshes the component containing the given column
     * @param columnIdx index of the column to be found
     */
    public void refreshColumn(final long columnIdx) {
        for (final Node n : columnBox.getChildren()) {
            final ColumnComponent cc = (ColumnComponent) n;
            if (cc.getColumn().getIndex() == columnIdx) {
                cc.refresh();
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
            final ColumnComponent cc = (ColumnComponent) n;
            for (final Node c : cc.getChildren()) {
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
        final Column column = new Column(getFunColumnName(), highestIndex++, new TreeSet<>());
        boardService.addColumnToCurrentBoard(column);
        mainCtrl.refreshOverview();
    }

    /**
     * Handles the tags button click
     */
    @FXML
    public void onTagsButtonClick() {

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

    /**
     * Refreshes indices of columns in an overview so that there are no gaps
     * @param removedIndex index of removed column
     */
    public void refreshIndices (final int removedIndex) {
        int indexCount = 0;
        for (final Column col : boardService.getCurrentBoard().getColumns()) {
            if  (indexCount >= removedIndex ) {
                col.setIndex(col.getIndex() - 1);
            }
            indexCount++;
        }
        highestIndex--;
    }
}
