package client.scenes.components.modals;

import client.Main;
import client.scenes.OverviewCtrl;
import client.scenes.components.UIComponent;
import client.services.BoardService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.datatransfer.StringSelection;

public class BoardSettingsModal extends Modal implements UIComponent {


    @FXML
    private TextField titleTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Text boardJoinKey;

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
     * Initialize modal
     */
    @FXML
    public void initialize() {
        super.initialize();
        this.titleTextField.setText(boardService.getCurrentBoard().getTitle());
        this.passwordTextField.setText(boardService.getCurrentBoard().getPassword());
        this.boardJoinKey.setText(boardService.getCurrentBoard().getJoinKey());
    }


    @FXML
    private void submitBoard() {
        boardService.renameBoard(titleTextField.getText());
        this.closeModal();
        parentCtrl.refresh();
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
}
