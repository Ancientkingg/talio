package commons;

import commons.exceptions.ColumnNotFoundException;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.SortedSet;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Board {
    @Id
    @NotBlank
    private String joinKey;
    private Timestamp created;
    @NotBlank
    private String title;
    @Size(min = 1) // A password cannot be empty, but it can be null (non-existent).
    private String password;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("index")
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
     * Getter for the board title
     * @return the board title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the board title
     * @param title title to be set
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Getter for the board join key
     *
     * @return Board join key
     */
    public String getJoinKey() {
        return joinKey;
    }

    /**
     * Getter for the creation date
     *
     * @return the date the board was created
     */
    public Timestamp getCreated() {
        return created;
    }

    /**
     * Getter for the board password
     *
     * @return Board password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for a new password
     *
     * @param passwords password to be set
     */
    public void setPassword(final String passwords) {
        this.password = passwords;
    }

    /**
     * Getter for the board columns
     *
     * @return Set containing CardLists
     */
    public SortedSet<Column> getColumns() {
        return columns;
    }

    /**
     * Finds a column by its heading
     * @param columnName the heading of the column to look for
     * @return the {@link Column} with {@code columnName} as {@code heading}
     */
    public Column getColumnByName(final String columnName) {
        if (columnName == null) return null;

        for (final Column column : columns) {
            if (column.getHeading().equals(columnName)) {
                return column;
            }
        }

        return null;
    }

    /**
     * Adds a card to the column with the name {@code columnName in the current board
     * @param card The card to add
     * @param columnName The column to add the card to
     * @throws ColumnNotFoundException When the requested column cannot be found in the board
     */
    public void addCardToColumn(final Card card, final String columnName) throws ColumnNotFoundException {
        final Column column = this.getColumnByName(columnName);

        if (column == null) throw new ColumnNotFoundException("The column " + columnName + " cannot be found in the board " + this.title);

        column.addCard(card);
    }

    /**
     * Setter for the board columns
     *
     * @param columns Set containing CardLists
     */
    public void setColumns(final SortedSet<Column> columns) {
        this.columns = columns;
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
