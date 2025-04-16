package animation.character.dto;

import java.util.List;

public record CharacterPageResponse(
        int totalPage,
        Long totalCount,
        int currentPage,
        int pageSize,
        List<CharacterResponse> characterResponseList
) {
}
