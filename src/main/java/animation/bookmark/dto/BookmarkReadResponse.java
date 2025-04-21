package animation.bookmark.dto;

import java.util.List;

public record BookmarkReadResponse(
        Long memberId,
        List<BookmarkData> bookmarks
) {
}
