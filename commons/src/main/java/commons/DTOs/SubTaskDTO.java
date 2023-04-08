package commons.DTOs;

import commons.SubTask;

public record SubTaskDTO(SubTask subTask, long cardId, int index) {

    /**
     * Constructor for subtask dto
     * @param subTask subtask
     * @param cardId id of card containing the subtask
     */
    public SubTaskDTO(final SubTask subTask, final long cardId) {
        this(subTask, cardId, 0);
    }
}
