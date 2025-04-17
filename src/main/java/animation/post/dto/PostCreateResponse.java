package animation.post.dto;

import java.time.LocalDateTime;

public record PostCreateResponse(
        Long boardId,
        Long postId,
        String postTitle,
        String userName,
        String postContent,
        LocalDateTime createdAt
) {
}
