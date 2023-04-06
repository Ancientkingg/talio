package server.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static server.Main.validatePassword;

@Controller
public class AdminController {

    private final Logger logger = LogManager.getLogger(AdminController.class);

    /**
     * Verifies password provided by user
     * @param password provided by user
     * @return correct/incorrect
     */
    @PostMapping("/admin/verify")
    public ResponseEntity<Boolean> verifyPassword (@RequestBody final String password) {
        logger.info("recieved request to switch to admin mode");
        return ResponseEntity.ok(validatePassword(password));
    }
}
