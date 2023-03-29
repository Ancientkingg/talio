package client.scenes;

import client.scenes.components.AddBoardCardButtonComponent;
import client.scenes.components.BoardCardComponent;
import client.scenes.components.EmptyBoardCardComponent;
import client.services.BoardService;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

import javax.inject.Inject;
import java.util.List;
import java.util.TreeSet;

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
     * Initializes the controller class. This method is automatically called
     */
    @FXML
    public void initialize() {
        this.renderBoards();
    }

    /**
     * Renders the boards
     */
    public void renderBoards() {
        innerBoardCardList.getChildren().clear();

        final List<Board> tempBoardList = List.of(new Board("joinkey","Board 1", null, new TreeSet<>()),
                new Board("joinkey","Board 2", null, new TreeSet<>()),
                new Board("joinkey","Board 3", null, new TreeSet<>()),
                new Board("joinkey","Board 4", null, new TreeSet<>()),
                new Board("joinkey","Board 5", null, new TreeSet<>()));

        final int rows = (int) Math.round((tempBoardList.size() + 2.0) / 4.0);

        for (int i = 0; i < rows * 4; i++) {

            if (i < tempBoardList.size()) { // if there are still boards to add

                final Board board = tempBoardList.get(i);
                final BoardCardComponent boardCard = new BoardCardComponent(board);
                innerBoardCardList.getChildren().add(boardCard);

            } else if (i == tempBoardList.size()) { // The add new board button

                final AddBoardCardButtonComponent boardCard = new AddBoardCardButtonComponent();
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

    }

    /**
     * Handles the disconnect button click
     */
    @FXML
    public void onDisconnectButtonClick() {

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

    }


}
