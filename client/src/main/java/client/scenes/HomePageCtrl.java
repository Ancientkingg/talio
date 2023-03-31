package client.scenes;

import client.scenes.components.AddBoardCardButtonComponent;
import client.scenes.components.BoardCardComponent;
import client.scenes.components.EmptyBoardCardComponent;
import client.services.BoardService;
import commons.Board;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

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
        mainCtrl.showOverview();
        mainCtrl.refreshOverview();
    }

    /**
     * Renders the boards
     */
    protected void renderBoards() {
        innerBoardCardList.getChildren().clear();

        final List<Board> boardList = boardService.getAllBoards();

        final int rows = (int) Math.round((boardList.size() + 2.0) / 4.0);

        for (int i = 0; i < rows * 4; i++) {

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
        mainCtrl.showJoinBoard();
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
