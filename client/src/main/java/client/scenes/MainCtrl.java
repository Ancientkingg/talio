package client.scenes;

import client.exceptions.BoardChangeException;
import commons.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class MainCtrl {

    private List<Board> boardList;
    private Board currentBoard;

    private Stage primaryStage;
    private Stage secondaryStage;
    private JoinBoardCtrl joinBoardCtrl;
    private Scene joinBoardScene;
    private OverviewCtrl overviewCtrl;
    private Scene overviewScene;
    private CreateColumnCtrl createColumnCtrl;
    private Scene createColumnScene;
    private CreateBoardCtrl createBoardCtrl;
    private Scene createBoardScene;
    private CreateTaskCtrl createTaskCtrl;
    private Scene createTaskScene;

    /**
     * Sets the primary stage upon launch and initializes each controller
     * with its corresponding scene.
     * @param primaryStage This should always be the overview and always be open
     * @param overview The main application page, from which to open other stages
     * @param joinBoard Join board page will initially be open upon booting to ensure some board
     *                  exists by the time one arrives at the overview
     * @param createBoard Create board page is an option from the join board page and creates
     *                    a new board which is loaded into the overview
     * @param createColumn The create column page is an option to add a column to a board
     *                     in the overview
     * @param createTask The create task page is an option to add a task to a column
     *                     in the overview
     */
    public void initialize(final Stage primaryStage, final Pair<OverviewCtrl, Parent> overview, final Pair<JoinBoardCtrl, Parent> joinBoard,
                           final Pair<CreateBoardCtrl, Parent> createBoard, final Pair<CreateColumnCtrl, Parent> createColumn,
                           final Pair<CreateTaskCtrl, Parent> createTask)
    {
        this.primaryStage = primaryStage;
        this.secondaryStage = new Stage();
        secondaryStage.initModality(Modality.APPLICATION_MODAL);

        this.overviewCtrl = overview.getKey();
        this.overviewScene = new Scene(overview.getValue());
        this.joinBoardCtrl = joinBoard.getKey();
        this.joinBoardScene = new Scene(joinBoard.getValue());
        this.createBoardCtrl = createBoard.getKey();
        this.createBoardScene = new Scene(createBoard.getValue());
        this.createColumnCtrl = createColumn.getKey();
        this.createColumnScene = new Scene(createColumn.getValue());
        this.createTaskCtrl = createTask.getKey();
        this.createTaskScene = new Scene(createTask.getValue());

        this.boardList = new LinkedList<>();

        showOverview();
        showJoinBoard();
    }

    /**
     * Shows overview stage in primaryStage
     */
    public void showOverview() {
        primaryStage.setTitle("Talio: Overview");
        primaryStage.setScene(overviewScene);
        primaryStage.show();
    }

    /**
     * Shows joinBoard stage in secondaryStage
     */
    public void showJoinBoard() {
        joinBoardCtrl.clearFields();
        secondaryStage.setTitle("Talio: Join Board");
        secondaryStage.setScene(joinBoardScene);
        secondaryStage.show();
    }

    /**
     * Shows createBoard stage in secondaryStage
     */
    public void showCreateBoard() {
        createBoardCtrl.clearFields();
        secondaryStage.setTitle("Talio: Create Board");
        secondaryStage.setScene(createBoardScene);
        secondaryStage.show();
    }

    /**
     * Shows createColumn stage in secondaryStage
     */
    public void showCreateColumn() {
        createColumnCtrl.clearFields();
        secondaryStage.setTitle("Talio: Create Column");
        secondaryStage.setScene(createColumnScene);
        secondaryStage.show();
    }

    /**
     * Shows createColumn stage in secondaryStage
     */
    public void showCreateTask() {
        createTaskCtrl.clearFields();
        createTaskCtrl.loadMenuItems();
        secondaryStage.setTitle("Talio: Create Task");
        secondaryStage.setScene(createTaskScene);
        secondaryStage.show();
    }

    /**
     * Closes secondaryStage regardless of what it is set to
     */
    public void closeSecondaryStage() {
        secondaryStage.close();
    }

    /**
     * Closes primaryStage, which should only happen on termination
     */
    public void closePrimaryStage() {
        primaryStage.close();
    }

    /**
     * Adds task to column
     * @param task Card to be added to column
     * @param col Column to be added to
     */
    public void addTask(final Card task, final Column col) throws BoardChangeException {
        if (!col.addCard(task)) {
            throw new BoardChangeException("Failed to add task : " + task);
        }
    }

    /**
     * Adds column to board in overview
     * @param col Column to be added
     */
    public void addColumn(final Column col) throws BoardChangeException {
        if (!currentBoard.addList(col)) {
            throw new BoardChangeException("Failed to add column : " + col);
        }
    }

    /**
     * Adds board to boardList
     * @param board Board to add
     */
    public void addBoard(final Board board) throws BoardChangeException {
        if (!boardList.add(board)) {
            throw new BoardChangeException("Failed to add board : " + board);
        }
    }

    /**
     * Sets the board displayed in overview stage to parameter and loads that board.
     * This method doesn't imply that a displayed board must also be in the boardList of
     * the overviewCtrl, which should always be the case. Depending on implementation later
     * on this may need to be adjusted.
     * @param board Board to be displayed
     */
    public void setCurrentBoard(final Board board) {
        currentBoard = board;
        refreshOverview();
    }

    /**
     * Gets currently loaded board
     * @return Currently loaded board as Board
     */
    public Board getCurrentBoard() { return currentBoard; }

    /**
     * Refreshes overview stage.
     * Is this terribly inefficient or just what it means to refresh by definition?
     */
    public void refreshOverview() {
        overviewCtrl.refresh();
    }

}
