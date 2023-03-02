package server.api;

import commons.Card;
import commons.Column;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.services.ColumnService;

import java.util.SortedSet;

@RestController
@RequestMapping("/columns")
public class ColumnController {
    private final ColumnService columnService;

    /**
     * Constructor for the Column Controller
     * @param columnService Dependency Injection for the column service
     */
    public ColumnController(final ColumnService columnService) {
        this.columnService = columnService;
    }

    /**
     * Create a Column
     * @param columnPostBody Column to be created
     * @return The Column saved in the ColumnRepository
     */
    @PostMapping("/add/column") // can be changed later
    public ResponseEntity<Column> addColumn(@RequestBody final Column columnPostBody) {

        String heading = columnPostBody.getHeading();
        int index = columnPostBody.getIndex();
        final SortedSet<Card> cards = columnPostBody.getCards();

        final Column column = new Column(heading, index, cards);

        final Column savedColumn = columnService.saveColumn(column);
        return ResponseEntity.ok(savedColumn);
    }
}
