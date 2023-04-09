package client.scenes.components.modals;

import client.Main;
import client.scenes.LiveUIController;
import client.scenes.OverviewCtrl;
import client.scenes.components.UIComponent;
import client.services.BoardService;
import commons.ColorScheme;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.datatransfer.StringSelection;

public class BoardSettingsModal extends Modal implements UIComponent, LiveUIController {


    @FXML
    private TextField titleTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Text boardJoinKey;

    @FXML
    private ColorPicker boardPrimaryColor;
    @FXML
    private ColorPicker boardSecondaryColor;
    @FXML
    private ColorPicker columnPrimaryColor;
    @FXML
    private ColorPicker columnSecondaryColor;
    @FXML
    private ColorPicker cardPrimaryColor;
    @FXML
    private ColorPicker cardSecondaryColor;

    private final OverviewCtrl parentCtrl;

    /**
     * Constructor for BoardSettingsModal
     * @param boardService boardService instance
     * @param parentScene parent scene (displayed under modal)
     * @param parentCtrl parent controller (used to refresh board)
     */
    public BoardSettingsModal(final BoardService boardService, final Scene parentScene, final OverviewCtrl parentCtrl) {
        super(boardService, parentScene);
        this.parentCtrl = parentCtrl;

        loadSource(Main.class.getResource("/components/BoardSettingsModal.fxml"));
    }

    /**
     * Refreshes the modal
     */
    public void refresh() {
        this.titleTextField.setText(boardService.getCurrentBoard().getTitle());
        this.passwordTextField.setText(boardService.getCurrentBoard().getPassword());
        this.boardJoinKey.setText(boardService.getCurrentBoard().getJoinKey());
        this.refreshColors();
    }

    /**
     * Initialize modal
     */
    @FXML
    public void initialize() {
        super.initialize();
        this.titleTextField.setText(boardService.getCurrentBoard().getTitle());
        this.passwordTextField.setText(boardService.getCurrentBoard().getPassword());
        this.boardJoinKey.setText(boardService.getCurrentBoard().getJoinKey());
        this.refreshColors();
    }

    private void refreshColors() {
        final ColorScheme boardColorScheme = boardService.getCurrentBoard().getBoardColorScheme();
        final ColorScheme columnColorScheme = boardService.getCurrentBoard().getColumnColorScheme();
        final ColorScheme cardColorScheme = boardService.getCurrentBoard().getCardColorScheme();

        final Color boardPrimary = Color.rgb(boardColorScheme.getBackgroundColor().getRed(),
                boardColorScheme.getBackgroundColor().getGreen(),
                boardColorScheme.getBackgroundColor().getBlue(),
                boardColorScheme.getBackgroundColor().getAlpha() / 255.0);

        final Color boardSecondary = Color.rgb(boardColorScheme.getTextColor().getRed(),
                boardColorScheme.getTextColor().getGreen(),
                boardColorScheme.getTextColor().getBlue(),
                boardColorScheme.getTextColor().getAlpha() / 255.0);

        final Color columnPrimary = Color.rgb(columnColorScheme.getBackgroundColor().getRed(),
                columnColorScheme.getBackgroundColor().getGreen(),
                columnColorScheme.getBackgroundColor().getBlue(),
                columnColorScheme.getBackgroundColor().getAlpha() / 255.0);

        final Color columnSecondary = Color.rgb(columnColorScheme.getTextColor().getRed(),
                columnColorScheme.getTextColor().getGreen(),
                columnColorScheme.getTextColor().getBlue(),
                columnColorScheme.getTextColor().getAlpha() / 255.0);

        final Color cardPrimary = Color.rgb(cardColorScheme.getBackgroundColor().getRed(),
                cardColorScheme.getBackgroundColor().getGreen(),
                cardColorScheme.getBackgroundColor().getBlue(),
                cardColorScheme.getBackgroundColor().getAlpha() / 255.0);

        final Color cardSecondary = Color.rgb(cardColorScheme.getTextColor().getRed(),
                cardColorScheme.getTextColor().getGreen(),
                cardColorScheme.getTextColor().getBlue(),
                cardColorScheme.getTextColor().getAlpha() / 255.0);

        this.boardPrimaryColor.setValue(boardPrimary);
        this.boardSecondaryColor.setValue(boardSecondary);
        this.columnPrimaryColor.setValue(columnPrimary);
        this.columnSecondaryColor.setValue(columnSecondary);
        this.cardPrimaryColor.setValue(cardPrimary);
        this.cardSecondaryColor.setValue(cardSecondary);
    }


    @FXML
    private void submitBoard() {
        boardService.renameBoard(titleTextField.getText());

        final ColorScheme boardColorScheme = convertToColorScheme(boardPrimaryColor, boardSecondaryColor);
        final ColorScheme columnColorScheme = convertToColorScheme(columnPrimaryColor, columnSecondaryColor);
        final ColorScheme cardColorScheme = convertToColorScheme(cardPrimaryColor, cardSecondaryColor);

        boardService.setDefaultColorPresetBoard(boardColorScheme);
        boardService.setDefaultColorPresetColumn(columnColorScheme);
        boardService.setDefaultColorPresetCard(cardColorScheme);

        this.closeModal();
        parentCtrl.refresh();
    }

    private ColorScheme convertToColorScheme(final ColorPicker primaryColor, final ColorPicker secondaryColor) {
        return new ColorScheme(
                new commons.Color((int) (secondaryColor.getValue().getRed() * 255),
                        (int) (secondaryColor.getValue().getGreen() * 255),
                        (int) (secondaryColor.getValue().getBlue() * 255),
                        (int) (secondaryColor.getValue().getOpacity() * 255)),
                new commons.Color((int) (primaryColor.getValue().getRed() * 255),
                        (int) (primaryColor.getValue().getGreen() * 255),
                        (int) (primaryColor.getValue().getBlue() * 255),
                        (int) (primaryColor.getValue().getOpacity() * 255))
        );
    }

    @FXML
    private void onJoinKeyClick() {
        final Point2D p = this.boardJoinKey.localToScreen(110, -32);

        final Tooltip customTooltip = new Tooltip("Copied join-key to clipboard!");
        customTooltip.setStyle("-fx-font-size: 11px");
        customTooltip.setAutoHide(false);
        customTooltip.show(this.boardJoinKey,p.getX(),p.getY());

        final PauseTransition pt = new PauseTransition(Duration.millis(750));
        pt.setOnFinished(e -> {
            customTooltip.hide();
        });
        pt.play();

        final StringSelection joinKeySelection = new StringSelection(boardService.getCurrentBoard().getJoinKey());
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(joinKeySelection, null);
    }

    @FXML
    private void onBoardColorReset() {
        this.boardPrimaryColor.setValue(Color.web("#ebebeb"));
        this.boardSecondaryColor.setValue(Color.web("#f3f3f3"));
    }

    @FXML
    private void onColumnColorReset() {
        this.columnPrimaryColor.setValue(Color.web("#f2f2f2"));
        this.columnSecondaryColor.setValue(Color.web("#181818"));
    }

    @FXML
    private void onCardColorReset() {
        this.cardPrimaryColor.setValue(Color.web("#f8f8f8"));
        this.cardSecondaryColor.setValue(Color.web("#0000004d"));
    }
}
