package animation.AniCharacter.dto;

import java.util.List;

public record CharAniPageResponse(
        int totalPage,
        Long totalCount,
        int currentPage,
        int pageSize,
        List<CharacterAnimesResponse> characterAnimesResponses
) {
}
