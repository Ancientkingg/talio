package server.api;

import commons.Board;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import server.services.AdminService;

import java.util.List;

import static server.Main.validatePassword;

@Controller
public class AdminController {

    private final Logger logger = LogManager.getLogger(AdminController.class);

    private final AdminService adminService;

    /**
     * Constructor for AdminController
     * @param adminService BoardService dependency injection
     */
    public AdminController (final AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Verifies password provided by user
     * @param password provided by user
     * @return correct/incorrect
     */
    @PostMapping("/admin/verify")
    public ResponseEntity<Boolean> verifyPassword (@RequestBody final String password) {
        logger.info("received request to switch to admin mode");
        return ResponseEntity.ok(validatePassword(password));
    }

    /**
     * gets all boards from the database
     * @return List of all Boards
     */
    @GetMapping("/admin/getAllBoards")
    public ResponseEntity<List<Board>> getAllBoards () {
        logger.info("received request to fetch all boards in server");
        final List<Board> allBoards = adminService.getAllBoards();
        return ResponseEntity.ok(allBoards);
    }
}
