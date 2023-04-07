package commons;

import commons.exceptions.ColumnNotFoundException;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Board {
    @Id
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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("index")
    @Getter @Setter
    private SortedSet<Column> columns;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ColorScheme columnTheme;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ColorScheme boardColorScheme;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Getter
    @Setter
    private Set<Tag> tags;

    /**
     * Constructor for a board object with password
     *
     * @param joinKey Key for joining
     * @param title Title of the board
     * @param password Password for the board
     * @param columns A set containing the board columns
     * @param columnTheme ColorScheme for column
     * @param boardColorScheme ColorScheme for board
     * @param created Timestamp for the board creation
     * @param tags Set of tags that can be used by cards in the board
     */
    public Board(final String joinKey, final String title, final String password, final SortedSet<Column> columns,
                 final ColorScheme columnTheme, final ColorScheme boardColorScheme, final Set<Tag> tags, final Timestamp created)
    {
        this.joinKey = joinKey;
        this.created = created;
        this.title = title;
        this.password = password;
        this.columns = columns;
        this.columnTheme = columnTheme;
        this.boardColorScheme = boardColorScheme;
        this.tags = tags;
    }

    /**
     * Constructor for a board object with password
     *
     * Sets default color theme
     *
     * @param joinKey Key for joining
     * @param title Title of the board
     * @param password Password for the board
     * @param columns A set containing the board columns
     * @param timestamp Timestamp for the board creation
     * @param tags Set of tags that can be used by cards in the board
     */
    public Board(final String joinKey, final String title, final String password, final SortedSet<Column> columns,
                 final Timestamp timestamp, final Set<Tag> tags)
    {
        this.joinKey = joinKey;
        this.title = title;
        this.password = password;
        this.columns = columns == null ? new TreeSet<>() : columns;
        this.created = timestamp;
        this.tags = (tags == null) ? new HashSet<>(0) : tags;

        columnTheme = new ColorScheme(new Color(0,0,0,255), new Color(255,255,255,255)); // change these to whatever default is picked
        boardColorScheme = new ColorScheme(new Color(0,0,0,255), new Color(255,255,255,255));
    }

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
        this(joinKey, title, password, columns, new Timestamp(System.currentTimeMillis()), new HashSet<>(0));
    }

    /**
     * Constructor for a board object with password and without tags
     * @param joinKey Key for joining
     * @param title Title of the board
     * @param password Password for the board
     * @param columns A set containing the board columns
     * @param timestamp Timestamp for the board creation
     */
    public Board(final String joinKey, final String title, final String password, final SortedSet<Column> columns, final Timestamp timestamp) {
        this(joinKey, title, password, columns, timestamp, new HashSet<>(0));
    }

    /**
     * Constructor for a board object with password
     * Sets the created date to now
     *
     * @param joinKey  Key for joining
     * @param title    Title of the board
     * @param password Password for the board
     * @param columns  A set containing the board columns
     * @param tags Set of tags that can be used by cards in the board
     */
    public Board(final String joinKey, final String title, final String password, final SortedSet<Column> columns, final Set<Tag> tags) {
        this(joinKey, title, password, columns, new Timestamp(System.currentTimeMillis()), tags);
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
     * Get a column of a board by id
     * @param columnId The id of the column to get
     * @return The column with the id {@code columnId}
     */
    public Column getColumnById(final long columnId) throws ColumnNotFoundException {

        for (final Column column : this.columns) {
            if (column.getId() == columnId) {
                return column;
            }
        }

        throw new ColumnNotFoundException("Column not found");
    }

    /**
     * Adds a card to the column with the name {@code columnName} in the current board
     * @param card The card to add
     * @param columnId The column to add the card to
     * @throws ColumnNotFoundException When the requested column cannot be found in the board
     */
    public void addCardToColumn(final Card card, final long columnId) throws ColumnNotFoundException {
        final Column column = this.getColumnById(columnId);

        column.addCard(card);
    }

    /**
     * Add one single column to the board if not already in the board
     * Null columns are not added
     *
     * @param column Column object
     *
     * @return success/failure
     */
    public boolean addColumn(final Column column) {
        return column != null && this.columns.add(column);
    }

    /**
     * Remove one list from the board
     * If column to be removed is null method returns directly since board cannot contain null columns
     * This is done to prevent a NullPointerException from being thrown by TreeSet.remove
     *
     * @param column List to remove
     *
     * @return success/failure
     */
    public boolean removeColumn(final Column column) {
        return column != null && this.columns.remove(column);
    }


    /**
     * Remove a tag from the board by id
     * @param tag The tag to remove
     * @return success/failure
     */
    public boolean removeTagById(final Tag tag) {
        if (tag == null) return false;
        for (final Tag t : this.tags) {
            if (tag.getId() == t.getId()) {
                return this.tags.remove(t);
            }
        }
        return false;
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

    /**
     * Adds a tag which can then be used by cards in that board
     *
     * @param tag the tag to be added to the board
     * @return success/failure
     */
    public boolean addTag(final Tag tag) {
        return tag != null && tags.add(tag);
    }

    /**
     * Deletes the tag from the board. Also deletes the tag from all cards that use the tag
     * Inefficient, yes.
     *
     * @param tag the tag to be deleted
     * @return success/failure
     */
    public boolean deleteTag(final Tag tag) {
        if (tag != null && tags.remove(tag)) {
            for (final Column column : columns)
                for (final Card card : column.getCards())
                    card.removeTag(tag);
            return true;
        }
        return false;
    }

    /**
     * Updates the tag in the board.
     * @param tag the tag to be updated
     */
    public void updateTag(final Tag tag) {
        for (final Tag t : tags)
            if (t.getId() == tag.getId()) {
                t.setColorScheme(tag.getColorScheme());
                t.setTitle(tag.getTitle());
                break;
            }
    }

    /**
     * Adds a tag to a card
     * @param cardId The id of the card to add the tag to
     * @param tag The tag to add to the card
     */
    public void addTagToCard(final long cardId, final Tag tag) {
        final Card card = this.getCard(cardId);
        if (card != null) {
            card.addTag(tag);
        }
    }

    /**
     * Removes a tag from a card
     * @param cardId The id of the card to remove the tag from
     * @param tag The tag to remove from the card
     */
    public void removeTagFromCard(final long cardId, final Tag tag) {
        final Card card = this.getCard(cardId);
        if (card != null) {
            card.removeTag(tag);
        }
    }

    /**
     * Refreshes indices of columns in an overview so that there are no gaps
     * @param removedIndex index of removed column
     */
    public void refreshIndices (final int removedIndex) {
        int indexCount = 0;
        for (final Column col : columns) {
            if  (indexCount >= removedIndex ) {
                col.setIndex(col.getIndex() - 1);
            }
            indexCount++;
        }
    }
}
