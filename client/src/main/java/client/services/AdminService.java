package client.services;

import client.models.BoardModel;
import client.scenes.MainCtrl;
import commons.Board;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
public class AdminService {
    private final BoardModel boardModel;
    private final ServerService serverService;
    private final MainCtrl mainCtrl;

    /**
     * Constructs an admin service
     *
     * @param serverService the injected server service
     * @param mainCtrl      the injected mainCtrl
     * @param boardModel injected instance of BoardModel
     */
    @Inject
    public AdminService(final BoardModel boardModel, final ServerService serverService, final MainCtrl mainCtrl) {
        this.boardModel = boardModel;
        this.serverService = serverService;
        this.mainCtrl = mainCtrl;
    }


    /**
     * Verifies password provided by user for switching to admin mode
     * @param adminPassword Password provided by user
     * @return correct/incorrect
     */
    public boolean verifyAdminPassword(final String adminPassword) {
        return serverService.verifyAdminPassword(adminPassword);
    }

    /**
     * Gets all Boards in the database from the server
     * @return List of all Boards
     */
    public List<Board> adminGetAllBoards() {
        return serverService.adminGetAllBoards();
    }

    /**
     * Loads all the Boards in the server for admin view
     */
    public void adminLoadAllBoards() {
        final List<Board> boards = this.adminGetAllBoards();
        this.boardModel.setBoardList(boards.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }
}
