package animation.board.dto;

import java.time.LocalDateTime;

public record BoardCreateResponse(
        Long boardId,
        String boardTitle,
        LocalDateTime createdAt
) {
}
