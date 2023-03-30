package commons.DTOs;

import lombok.Getter;

public record ColumnDTO (@Getter Long columnId, @Getter String newHeading) {
}
