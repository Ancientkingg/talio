package commons.DTOs;

import commons.ColorScheme;
import lombok.Getter;

public record ColorSchemeDTO(@Getter ColorScheme colorScheme, @Getter String password, @Getter Long cardId) {
    /**
     * Used to transport tag with password from client to server
     * @param colorScheme Color scheme being changed
     * @param password String for board
     */
    public ColorSchemeDTO(final ColorScheme colorScheme, final String password) {
        this(colorScheme, password, null);
    }

    /**
     * Used to transport tag and the cardId the tag is from
     * @param colorScheme Color scheme being changed
     * @param cardId Long for the card the tag belongs to
     */
    public ColorSchemeDTO(final ColorScheme colorScheme, final Long cardId) {
        this(colorScheme, null, cardId);
    }
}
