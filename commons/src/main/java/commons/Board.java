package commons;

import commons.exceptions.ColumnNotFoundException;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.SortedSet;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Board {
    @Id
    @NotBlank
    @Getter
    private String joinKey;
    @Getter
    private Timestamp created;
    @NotBlank
    @Getter @Setter
    private String title;
    @Getter @Setter
    @Size(min = 1) // A password cannot be empty, but it can be null (non-existent).
    private String password;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
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
     * Constructor for a board object with password
     * @param joinKey Key for joining
     * @param title Title of the board
     * @param password Password for the board
     * @param columns A set containing the board columns
     * @param timestamp Timestamp for the board creation
     */
    public Board(final String joinKey, final String title, final String password, final SortedSet<Column> columns, final Timestamp timestamp) {
        this.joinKey = joinKey;
        this.title = title;
        this.password = password;
        this.columns = columns;
        this.created = timestamp;
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
    public boolean addColumn(final Column cardList) {
        return this.columns.add(cardList);
    }

    /**
     * Remove one list from the board
     *
     * @param cardList List to remove
     *
     * @return success/failure
     */
    public boolean removeColumn(final Column cardList) {
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
                Objects.equals(password, board.password) &&
                Objects.equals(columns, board.columns);
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
