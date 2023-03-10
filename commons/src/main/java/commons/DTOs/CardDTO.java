package commons.DTOs;

import commons.Card;
import lombok.Getter;

public class CardDTO {

    @Getter
    private final Card card;

    @Getter
    private final String password;

    /**
     * Constructor for CardDTO
     * @param card Card to be transferred
     * @param password password of board in which card belongs for authentication
     */
    public CardDTO(final Card card, final String password) {
        this.card = card;
        this.password = password;
    }
}
