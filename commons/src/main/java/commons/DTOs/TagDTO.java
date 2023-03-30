package commons.DTOs;

import commons.Tag;
import lombok.Getter;

public record TagDTO(@Getter Tag tag, @Getter String password) {
}
