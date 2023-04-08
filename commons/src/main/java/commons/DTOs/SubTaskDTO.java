package commons.DTOs;

import commons.SubTask;

public record SubTaskDTO(SubTask subTask, long cardId, int index) {

    public SubTaskDTO(final SubTask subTask, final long cardId) {
        this(subTask, cardId, 0);
    }
}
