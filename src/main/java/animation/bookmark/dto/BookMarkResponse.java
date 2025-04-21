package animation.bookmark.dto;

import java.time.LocalDateTime;

public record BookMarkResponse(
        Long aniId,
        Long memberId,
        LocalDateTime createAt
) {
}
