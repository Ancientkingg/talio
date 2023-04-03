package client.scenes.components;

import client.Main;
import client.scenes.OverviewCtrl;
import client.services.BoardService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.datatransfer.StringSelection;
import java.io.IOException;

public class BoardSettingsModal extends Modal {


    @FXML
    private TextField titleTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Text boardJoinKey;

    private final OverviewCtrl parentCtrl;

    public BoardSettingsModal(BoardService boardService, Scene parentScene, OverviewCtrl parentCtrl) {
        super(boardService, parentScene);
        this.parentCtrl = parentCtrl;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/BoardSettingsModal.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
