package animation.AniCharacter.dto;

import java.util.List;

public record AniCharPageResponse(
        int totalPage,
        Long totalCount,
        int currentPage,
        int pageSize,
        List<AnimeCharactersResponse> animeCharactersResponses
) {
}
