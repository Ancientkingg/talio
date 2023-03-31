package commons.DTOs;

import commons.Tag;
import lombok.Getter;

public record TagDTO(@Getter Tag tag, @Getter String password, @Getter Long cardId) {
    public TagDTO(final Tag tag, final String password) {
        this(tag, password, null);
    }

    public TagDTO(final Tag tag, final Long cardId) {
        this(tag, null, cardId);
    }
}
