package client.scenes;

import client.scenes.components.AddBoardCardButtonComponent;
import client.scenes.components.BoardCardComponent;
import client.scenes.components.EmptyBoardCardComponent;
import client.scenes.components.JoinBoardModal;
import client.services.BoardService;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.util.List;

public class HomePageCtrl {

    private final MainCtrl mainCtrl;

    private final BoardService boardService;

    @FXML
    private FlowPane innerBoardCardList;

    @FXML
    private Button joinBoardButton;

    @FXML
    private Button disconnectButton;

    @FXML
    private Button settingsButton;

    private long lastTimeResize;



    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     * @param boardService Shared instance of BoardService
     */
    @Inject
    public HomePageCtrl(final MainCtrl mainCtrl, final BoardService boardService) {
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
    }

    @FXML
    public void initialize() {
        this.addResizeListener();
    }

    /**
     * Loads the boards from the server and renders them
     */
    public void loadBoards() {
        boardService.loadBoardsForCurrentServer();
        this.renderBoards();
    }

    /**
     * Loads the board and shows the overview
     * @param board The board to load
     */
    public void loadBoard(final Board board) {
        boardService.saveBoardsLocal();
        boardService.setCurrentBoard(board);
        boardService.subscribeToBoard(board.getJoinKey());
        mainCtrl.showOverview();
        mainCtrl.refreshOverview();
    }

    private void addResizeListener() {
        this.innerBoardCardList.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (System.currentTimeMillis() > this.lastTimeResize && System.currentTimeMillis() - this.lastTimeResize < 125) return;
            this.renderBoards();
            this.lastTimeResize = System.currentTimeMillis();
        });
    }

    /**
     * Renders the boards
     */
    protected void renderBoards() {
        this.innerBoardCardList.getChildren().clear();

        final List<Board> boardList = boardService.getAllBoards();

        final Stage primaryStage = mainCtrl.getPrimaryStage();
        final double windowWidth = primaryStage.getWidth();
        final double homePageWidth = windowWidth - this.innerBoardCardList.getPadding().getLeft();
        final int rowSize = (int) Math.floor(homePageWidth / 313);


        final int rows = (int) Math.ceil((boardList.size() + 1.0) / rowSize);

        for (int i = 0; i < rows * rowSize; i++) {

            if (i < boardList.size()) { // if there are still boards to add

                final Board board = boardList.get(i);
                final BoardCardComponent boardCard = new BoardCardComponent(board, this);
                innerBoardCardList.getChildren().add(boardCard);

            } else if (i == boardList.size()) { // The add new board button

                final AddBoardCardButtonComponent boardCard = new AddBoardCardButtonComponent(boardService, this);
                innerBoardCardList.getChildren().add(boardCard);

            } else { // empty invisible board cards for UI alignment purposes.

                final BoardCardComponent boardCard = new EmptyBoardCardComponent();
                innerBoardCardList.getChildren().add(boardCard);

            }
        }
    }

    /**
     * Handles the join board button click
     */
    @FXML
    public void onJoinBoardButtonClick() {
        final Scene parentScene = mainCtrl.getCurrentScene();
        final JoinBoardModal modal = new JoinBoardModal(boardService, parentScene, this);
        modal.showModal();
    }

    /**
     * Handles the disconnect button click
     */
    @FXML
    public void onDisconnectButtonClick() {
        boardService.saveBoardsLocal();
        boardService.disconnect();
        mainCtrl.showJoinServer();
    }

    /**
     * Handles the settings button click
     */
    @FXML
    public void onSettingsButtonClick() {

    }


    /**
     * Refreshes the UI component
     */
    public void refresh() {
        this.renderBoards();
    }


}
