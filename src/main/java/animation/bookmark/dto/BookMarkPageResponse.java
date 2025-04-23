package animation.bookmark.dto;

import animation.character.dto.CharacterResponse;

import java.util.List;

public record BookMarkPageResponse(
        int totalPage,
        Long totalCount,
        int currentPage,
        int pageSize,
        BookmarkReadResponse bookmarkReadResponse
) {
}
