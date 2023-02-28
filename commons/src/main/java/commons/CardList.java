package commons;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class CardList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String heading;
    @OneToMany
    private Set<Card> cards;

    /**
     * Empty constructor for the Card List
     */
    public CardList() {

    }

    /**
     * Constructor for the CardList/Column object
     * @param heading Heading for the list
     * @param cards A set of cards contained by the list
     */
    public CardList(final String heading, final Set<Card> cards) {
        this.heading = heading;
        this.cards = cards;
    }

    /**
     * Getter for ID
     * @return List ID
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for the heading
     * @return List heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * Setter for the list heading
     * @param heading Heading to be set
     */
    public void setHeading(final String heading) {
        this.heading = heading;
    }

    /**
     * Getter for the cards in the list
     * @return List cards
     */
    public Set<Card> getCards() {
        return cards;
    }

    /**
     * Setter for the list cards
     * @param cards A set containing the cards to be set in the list
     */
    public void setCards(final Set<Card> cards) {
        this.cards = cards;
    }

    /**
     * Add one single card to the list if not already in the list
     * @param card Card to be added
     *
     * @return success/failure
     */
    public boolean addCard(final Card card) {
        return this.cards.add(card);
    }

    /**
     * Remove one card from the list
     * @param card card to remove

     * @return success/failure
     */
    public boolean removeCard(final Card card) {
        return this.cards.remove(card);
    }

    /**
     * Checks for equality of two lists
     * @param o Other list
     *
     * @return is this list equal to the other list?
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CardList cardList = (CardList) o;
        return id == cardList.id && heading.equals(cardList.heading) && cards.equals(cardList.cards);
    }

    /**
     * Hash code generator for the List
     * @return generated hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, heading, cards);
    }
}
