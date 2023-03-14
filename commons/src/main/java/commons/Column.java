package commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Column implements Comparable<Column> {
    @Id
    @NotBlank
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;
    @Getter @Setter
    @NotNull
    private int index;

    @Getter @Setter
    @NotBlank
    private String heading;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("priority")
    @Getter @Setter
    private SortedSet<Card> cards;

    /**
     * Empty constructor for the Column
     */
    protected Column() {

    }

    /**
     * Constructor for the Column/Column object
     * @param heading Heading for the column
     * @param index Index of the column in the board
     * @param cards A set of cards contained by the column
     */
    public Column(final String heading, final int index, final SortedSet<Card> cards) {
        this.heading = heading;
        this.index = index;
        this.cards = cards;
    }

    /**
     * Add one single card to the column if not already in the column
     * @param card Card to be added
     *
     * @return success/failure
     */
    public boolean addCard(final Card card) {
        return this.cards.add(card);
    }

    /**
     * Remove one card from the column
     * @param card card to remove

     * @return success/failure
     */
    public boolean removeCard(final Card card) {
        return this.cards.remove(card);
    }

    /**
     * Checks for equality of two columns
     * @param o Other column
     *
     * @return is this column equal to the other column?
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Column column = (Column) o;
        return id == column.id && index == column.index
                && heading.equals(column.heading) && cards.equals(column.cards);
    }

    /**
     * Hash code generator for the column
     * @return generated hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, index, heading, cards);
    }

    /**
     * Compares two columns on the basis of
     * their index in the board
     * @param o the object to be compared.
     *
     * @return 0 if the objects have the same index,
     * 1 if this object has a higher index,
     * -1 if the other object has a higher index
     */
    @Override
    public int compareTo(final Column o) {
        return Integer.compare(index, o.index);
    }

    /**
     * Swap the priority of two cards
     * @param card1 card to be swapped
     * @param card2 card to be swapped
     */
    public void swapCards(final Card card1, final Card card2) {
        final int index1 = card1.getPriority();
        card1.setPriority(card2.getPriority());
        card2.setPriority(index1);
    }
}
