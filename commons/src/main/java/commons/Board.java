package commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.SortedSet;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Board {
    @Id
    @Getter
    private String joinKey;
    @Getter
    private Timestamp created;
    @Getter @Setter
    private String title;
    @Getter @Setter
    private String password;
    @OneToMany
    @OrderBy("index")
    @Getter @Setter
    private SortedSet<Column> columns;

    /**
     * Constructor for the board object.
     * Sets the created date to now
     */
    protected Board() {
        this.created = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Constructor for a board object with password
     * Sets the created date to now
     *
     * @param joinKey  Key for joining
     * @param title    Title of the board
     * @param password Password for the board
     * @param columns  A set containing the board columns
     */
    public Board(final String joinKey, final String title, final String password, final SortedSet<Column> columns) {
        this.joinKey = joinKey;
        this.title = title;
        this.password = password;
        this.columns = columns;
        this.created = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Constructor for a board object without a password
     * Sets the created date to now
     *
     * @param joinKey Key for joining
     * @param title   Title for the board
     * @param columns A set containing the board columns
     */
    public Board(final String joinKey, final String title, final SortedSet<Column> columns) {
        this.joinKey = joinKey;
        this.title = title;
        this.password = null;
        this.columns = columns;
        this.created = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Add one single list to the board if not already in the board
     *
     * @param cardList Column object
     *
     * @return success/failure
     */
    public boolean addList(final Column cardList) {
        return this.columns.add(cardList);
    }

    /**
     * Remove one list from the board
     *
     * @param cardList List to remove
     *
     * @return success/failure
     */
    public boolean removeList(final Column cardList) {
        return this.columns.remove(cardList);
    }

    /**
     * Checks for equality between two boards
     *
     * @param o Other board
     *
     * @return is this board equal to the other board?
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Board board = (Board) o;
        return joinKey.equals(board.joinKey) &&
                title.equals(board.title) &&
                created.equals(board.created) &&
                password.equals(board.password) &&
                columns.equals(board.columns);
    }

    /**
     * Generator for the board hash code
     *
     * @return Generated hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(joinKey, title, created, password, columns);
    }
}
