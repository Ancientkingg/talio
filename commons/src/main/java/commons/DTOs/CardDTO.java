package commons.DTOs;

import commons.Card;
import lombok.Getter;

public record CardDTO(@Getter Card card, @Getter String password) {
}
