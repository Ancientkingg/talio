package commons;

import commons.exceptions.ColumnNotFoundException;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Board {
    @Id
    @NotBlank
    @Getter
    private String joinKey;
    @Getter
    private final Timestamp created;
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
        this.columns = columns == null ? new TreeSet<>() : columns;
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
        this(joinKey, title, null, columns, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Get a column of a board by name
     * @param columnName The name of the column to get
     * @return The column with the name {@code columnName} or null if not found
     */
    public Column getColumnByName(final String columnName) {
        if (columnName == null) return null;

        for (final Column column : this.columns) {
            if (Objects.equals(column.getHeading(), columnName)) {
                return column;
            }
        }

        return null;
    }

    /**
     * Adds a card to the column with the name {@code columnName} in the current board
     * @param card The card to add
     * @param columnName The column to add the card to
     * @throws ColumnNotFoundException When the requested column cannot be found in the board
     */
    public void addCardToColumn(final Card card, final String columnName) throws ColumnNotFoundException {
        final Column column = this.getColumnByName(columnName);

        if (column == null) throw new ColumnNotFoundException("The column " + columnName + " cannot be found in the board" + this.title);

        column.addCard(card);
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

    /**
     * Get a card by its id
     * @param cardId The id of the card to get
     *
     * @return The card with the id {@code cardId} or null if not found
     */
    public Card getCard(final long cardId) {
        for (final Column column : this.columns) {
            for (final Card card : column.getCards()) {
                if (card.getId() == cardId) {
                    return card;
                }
            }
        }

        return null;
    }

    /**
     * Get a column by its index
     * @param columnIdx The index of the column to get
     *
     * @return The column with the index {@code columnIdx} or null if not found
     */
    public Column getColumn(final long columnIdx) {
        for (final Column column : this.columns) {
            if (column.getIndex() == columnIdx) {
                return column;
            }
        }

        return null;
    }
}
