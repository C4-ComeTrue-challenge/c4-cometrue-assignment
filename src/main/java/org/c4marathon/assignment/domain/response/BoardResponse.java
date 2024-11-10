package org.c4marathon.assignment.domain.response;

import lombok.Data;
import org.c4marathon.assignment.domain.Board;

@Data
public class BoardResponse {
    private Long boardId;
    private String boardName;
    private String description;

    public BoardResponse(Board board) {
        this.boardId = board.getBoardId();
        this.boardName = board.getBoardName();
        this.description = board.getDescription();
    }
}
