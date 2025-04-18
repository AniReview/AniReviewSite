package animation.board.dto;

import java.time.LocalDateTime;

public record BoardUpdateResponse(
        Long boardId,
        String boardTitle,
        LocalDateTime updatedAt
) {
}
