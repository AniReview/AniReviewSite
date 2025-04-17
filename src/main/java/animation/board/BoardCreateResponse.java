package animation.board;

import java.time.LocalDateTime;

public record BoardCreateResponse(
        Long boardId,
        String boardTitle,
        LocalDateTime createdAt
) {
}
