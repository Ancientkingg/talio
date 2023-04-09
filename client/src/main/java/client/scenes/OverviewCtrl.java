package client.scenes;

import client.exceptions.BoardChangeException;
import client.scenes.components.Draggable;
import client.scenes.components.modals.BoardSettingsModal;
import client.scenes.components.CardComponent;
import client.scenes.components.ColumnComponent;
import client.scenes.components.modals.*;
import client.services.BoardService;
import commons.Card;
import commons.Column;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import lombok.Getter;

import javax.inject.Inject;
import java.awt.datatransfer.StringSelection;
import java.util.TreeSet;

public class OverviewCtrl implements Refreshable {
    private final MainCtrl mainCtrl;
    private final BoardService boardService;

    @Getter
    private CardComponent focussedCard;

    @FXML
    private HBox columnBox;
    @FXML
    private Button linkButton;
    
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
        focussedCard = null;
    }

    /**
     * Setter for focussedCard
     * @param cardComponent cardComponent to be focussed on next
     */
    public void setFocussedCard (final CardComponent cardComponent) {

        if (focussedCard != null) {
            focussedCard.getStyleClass().remove("selectedCard"); // remove focus from currently focussed card component
            focussedCard.toggleCardEditing(true); // disable card editing
        }

        if (cardComponent != null) { // null is passed if the last card in a column has focus and is deleted
            cardComponent.getStyleClass().add("selectedCard");
            cardComponent.requestFocus();
        }

        focussedCard = cardComponent;
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
     * Set up keyboard shortcuts for overview
     *
     * cannot be called from initialize() because it requires overviewScene to have been initialized which is not the case when initialize is called.
      */
    public void setKeyboardShortcuts() {

        // for shortcuts which are a combination of keys
        final ObservableMap<KeyCombination, Runnable> keyboardShortcuts = mainCtrl.getCurrentScene().getAccelerators();

        setHelpModalShortcut(keyboardShortcuts);
        setShiftingCardsKeyboardShortcuts(keyboardShortcuts);

        // single key shortcuts
        mainCtrl.getCurrentScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            if (focussedCard != null) {

                switch (event.getCode()) {

                    case ENTER -> setOpenCardDetailsShortcut();

                    case E -> focussedCard.toggleCardEditing(false);

                    case DELETE, BACK_SPACE -> setDeleteCardShortcut();

                    case DOWN -> setSelectCardBelowShortcut();

                    case UP -> setSelectCardAboveShortcut();

                    case LEFT -> setSelectCardOnLeftShortcut();

                    case RIGHT -> setSelectCardOnRightShortcut();

                    case T -> setTagForCardShortcut();

                    case C -> { /*TODO*/ }
                }
            }
        });

    }

    private void setTagForCardShortcut() {
        final TagsShortcutModal tagsShortcutModal = new TagsShortcutModal(boardService, mainCtrl.getCurrentScene(), this);

        tagsShortcutModal.showModal();
    }

    private void setHelpModalShortcut(final ObservableMap<KeyCombination, Runnable> keyboardShortcuts) {
        final KeyCombination helpMenu = new KeyCodeCombination(KeyCode.SLASH, KeyCombination.CONTROL_ANY, KeyCombination.SHIFT_DOWN);
        final Runnable showHelpModal = () -> {
            try {
                final ShortcutsHelpModal shortcutsHelpModal = new ShortcutsHelpModal(boardService, mainCtrl.getCurrentScene());
                shortcutsHelpModal.showModal();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        keyboardShortcuts.put(helpMenu, showHelpModal);
    }

    private void setOpenCardDetailsShortcut() {
        final CardDetailsModal modal = new CardDetailsModal(boardService, focussedCard.getColumnParent().getScene(),
                focussedCard.getCard(), focussedCard);
        modal.showModal();
    }

    private void setSelectCardBelowShortcut() {
        final ObservableList<Node> children = focussedCard.getColumnParent().getInnerCardList().getChildren();
        final int index = children.indexOf(focussedCard);
        if (index < children.size() - 1) // not last
            setFocussedCard(((CardComponent) children.get(index + 1)));
    }

    private void setSelectCardAboveShortcut() {
        final ObservableList<Node> children = focussedCard.getColumnParent().getInnerCardList().getChildren();
        final int index = children.indexOf(focussedCard);
        if (index > 0) // not last
            setFocussedCard(((CardComponent) children.get(index - 1)));
    }

    private void setDeleteCardShortcut() {
        final ObservableList<Node> children = focussedCard.getColumnParent().getInnerCardList().getChildren();
        final int index = children.indexOf(focussedCard);

        final Card cardToBeRemoved = focussedCard.getCard();
        final Column column = focussedCard.getColumnParent().getColumn();

        // if empty, set focussedCard to null, else (if last card was deleted, then set focussedCard to card just before it else next card)
        setFocussedCard((children.size() == 1) ? null : (CardComponent) children.get(Math.max(0, Math.min(children.size() - 1, index))));

        boardService.removeCardFromColumn(cardToBeRemoved, column);

        refreshColumn(column.getId());
    }

    private void setSelectCardOnRightShortcut() {
        final ObservableList<Node> currentColumn = focussedCard.getColumnParent().getInnerCardList().getChildren();
        int columnIndex = columnBox.getChildren().indexOf(focussedCard.getColumnParent()); // index of column containing the focussed card
        int rowIndex = currentColumn.indexOf(focussedCard); // index of focussed card
        ObservableList<Node> rightColumn;

        while (columnIndex ++ < columnBox.getChildren().size() - 1) { // not last, meaning there is a column to the right
            rightColumn = ((ColumnComponent) columnBox.getChildren().get(columnIndex))
                    .getInnerCardList().getChildren(); // column to the right

            if (rightColumn == null || rightColumn.size() == 0) // if right column has no cards, check next column (more to the right)
                continue;
            else rowIndex = Math.min(rightColumn.size() - 1, rowIndex); // last card if column to right doesn't have enough cards

            setFocussedCard(((CardComponent) rightColumn.get(rowIndex))); // set the focus
            break;
        }
    }

    private void setSelectCardOnLeftShortcut() {
        // column containing current card with focus
        final ObservableList<Node> currentColumn = focussedCard.getColumnParent().getInnerCardList().getChildren();
        int columnIndex = columnBox.getChildren().indexOf(focussedCard.getColumnParent()); // index of column containing the focussed card
        int rowIndex = currentColumn.indexOf(focussedCard); // index of focussed card
        ObservableList<Node> leftColumn;

        while (columnIndex -- > 0) { // not first, meaning there is a column to the left
            leftColumn = ((ColumnComponent) columnBox.getChildren().get(columnIndex))
                    .getInnerCardList().getChildren(); // column to the left

            if (leftColumn == null || leftColumn.size() == 0) // if left column has no cards, try to check for columns further to the left
                continue;
            else // if column to the left has cards, move focus to a card in it
                rowIndex = Math.min(leftColumn.size() - 1, rowIndex); // last card if column to left does not have enough cards,
            // else card at same index
            setFocussedCard(((CardComponent) leftColumn.get(rowIndex))); // set the focus
            break;
        }
    }

    private void setShiftingCardsKeyboardShortcuts(final ObservableMap<KeyCombination, Runnable> keyboardShortcuts) {

        final KeyCombination shiftCardUp = new KeyCodeCombination(KeyCode.UP, KeyCombination.SHIFT_DOWN);
        final Runnable moveCardUp = () -> {
            if (focussedCard != null) {
                boardService.repositionCard(focussedCard.getCard().getId(),
                        focussedCard.getColumnParent().getColumn().getId(), focussedCard.getColumnParent().getColumn().getId(),
                        Math.max(0, focussedCard.getCard().getPriority() - 1)); // non-negative priority
            }
        };
        keyboardShortcuts.put(shiftCardUp, moveCardUp);

        final KeyCombination shiftCardDown = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.SHIFT_DOWN);
        final Runnable moveCardDown = () -> {
            if (focussedCard != null) {
                boardService.repositionCard(focussedCard.getCard().getId(),
                        focussedCard.getColumnParent().getColumn().getId(), focussedCard.getColumnParent().getColumn().getId(),
                        focussedCard.getCard().getPriority() + 1);
            }
        };
        keyboardShortcuts.put(shiftCardDown, moveCardDown);
    }

    /**
     * Refreshes the overview
     */
    public void refresh() {
        this.boardNameButton.setText(boardService.getCurrentBoard().getTitle());
        ((StackPane) mainCtrl.getCurrentScene().getRoot()).getChildren()
            .removeIf(c -> c instanceof Draggable);
        this.refreshColumn();
    }

    /**
     * Refreshes the overview scene columnBox by iterating over each column in the current board
     * and displaying the corresponding titles. Will also refresh cards in the future.
     */
    public void refreshColumn() {
        columnBox.getChildren().removeAll(columnBox.getChildren().stream().filter(c -> c instanceof ColumnComponent).toList());

        for (final Column col : boardService.getCurrentBoard().getColumns()) {
            final ColumnComponent columnComponent = new ColumnComponent(boardService, col, this, mainCtrl.getCurrentScene());

            columnComponent.setHeading(col.getHeading());

            columnBox.getChildren().add(columnBox.getChildren().size() - 1,columnComponent);
        }
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
        setFocussedCard(focussedCard);
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
     * Handles the link button click
     */
    @FXML
    public void onLinkButtonClick() {
        final Point2D p = linkButton.localToScreen(-110, 32);

        final Tooltip customTooltip = new Tooltip("Copied join-key to clipboard!");
        customTooltip.setAutoHide(false);
        customTooltip.show(linkButton,p.getX(),p.getY());

        final PauseTransition pt = new PauseTransition(Duration.millis(1250));
        pt.setOnFinished(e -> customTooltip.hide());
        pt.play();

        final StringSelection joinKeySelection = new StringSelection(boardService.getCurrentBoard().getJoinKey());
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(joinKeySelection, null);
    }

    @FXML
    private void onBoardEditButtonClick() {
        final BoardSettingsModal modal = new BoardSettingsModal(boardService, this.mainCtrl.getCurrentScene(), this);
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
