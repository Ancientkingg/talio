package server.longPolling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.api.exceptions.ResourceNotFoundException;
import server.services.BoardService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
public class HomePageController {
    private final BoardService boardService;
    private final Logger logger = LogManager.getLogger(HomePageController.class);

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * Constructor for HomePageController
     * @param boardService BoardService dependency injection
     */
    public HomePageController(final BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Checks if the boards exist
     * @param joinKeys list of join keys to check
     *
     * @return list of key-value pairs where the key is the join key and the value is true if the board exists
     */
    @PostMapping("/home/getBoardsStatus")
    public DeferredResult<HashMap<String, Boolean>> getBoardsStatus(
            final @Valid @RequestBody List<String> joinKeys)
    {
        logger.info("received request to check if boards exist: " + joinKeys.toString());
        final DeferredResult<HashMap<String, Boolean>> deferredResult = new DeferredResult<>();

        executorService.execute(() -> {
            final HashMap<String, Boolean> result = new HashMap<>();
            for (final String joinKey : joinKeys) {
                try {
                    boardService.getBoardWithKey(joinKey);
                    result.put(joinKey, true);
                } catch (ResourceNotFoundException e) {
                    result.put(joinKey, false);
                }
            }
            logger.info("returning result: " + result);
            deferredResult.setResult(result);
        });
        return deferredResult;
    }
}
