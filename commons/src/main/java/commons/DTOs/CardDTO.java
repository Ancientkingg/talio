package commons.DTOs;

import commons.Card;
import lombok.Getter;

public record CardDTO(@Getter Card card, @Getter String password, @Getter Long columnFromId, @Getter Long columnToId, @Getter Integer newPosition) {
    public CardDTO(final Card card, final String str1) {
        this(card, str1, null, null, null);
    }

    public CardDTO(final Card card, final Long long1) {
        this(card, null, long1, null, null);
    }

    public CardDTO(final Card card, final Long long1, final Long long2, final Integer int1) {
        this(card, null, long1, long2, int1);
    }
}
