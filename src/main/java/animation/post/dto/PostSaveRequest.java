package animation.post.dto;

public record PostSaveRequest(
        Long boardId,
        String postTitle,
        String postContent

) {
}
