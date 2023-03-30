package commons.DTOs;

import commons.Card;
import lombok.Getter;

public record CardDTO(@Getter Card card, @Getter String password, @Getter Long columnFromId, @Getter Long columnToId, @Getter Integer newPosition) {
    /**
     * Used to pass password with card
     * @param card Card
     * @param str1 String password
     */
    public CardDTO(final Card card, final String str1) {
        this(card, str1, null, null, null);
    }

    /**
     * Used to pass column id of card with card
     * @param card Card
     * @param long1 Long id
     */
    public CardDTO(final Card card, final Long long1) {
        this(card, null, long1, null, null);
    }

    /**
     * Used to pass column id of card, new column id, and priority
     * @param card Card
     * @param long1 Long column id
     * @param long2 Long new column id
     * @param int1 Integer priority of card
     */
    public CardDTO(final Card card, final Long long1, final Long long2, final Integer int1) {
        this(card, null, long1, long2, int1);
    }
}
