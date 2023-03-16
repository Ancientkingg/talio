package commons.DTOs;

import commons.Card;
import lombok.Getter;

import java.util.List;

public record CardListDTO(@Getter List<Card> cards, @Getter String password) {
    /**
     * Constructor for CardDTO
     *
     * @param cards    Card to be transferred
     * @param password password of board in which card belongs for authentication
     */
    public CardListDTO {
    }
}
