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
    Set<Card> cards;

    public CardList() {}

    public CardList(String heading, Set<Card> cards) {
        this.heading = heading;
        this.cards = cards;
    }

    public long getId() {
        return id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public boolean addCard(Card card) {
        return this.cards.add(card);
    }

    public boolean removeCard(Card card) {
        return this.cards.remove(card);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardList cardList = (CardList) o;
        return id == cardList.id && heading.equals(cardList.heading) && cards.equals(cardList.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, heading, cards);
    }
}
