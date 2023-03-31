package commons.DTOs;

import commons.Tag;
import lombok.Getter;

public record TagDTO(@Getter Tag tag, @Getter String password, @Getter Long cardId) {
    /**
     * Used to transport tag with password from client to server
     * @param tag Tag being chnaged
     * @param password String for board
     */
    public TagDTO(final Tag tag, final String password) {
        this(tag, password, null);
    }

    /**
     * Used to transport tag and the cardId the tag is from
     * @param tag Tag being changed
     * @param cardId Long for the card the tag belongs to
     */
    public TagDTO(final Tag tag, final Long cardId) {
        this(tag, null, cardId);
    }
}
