package animation.AniCharacter.dto;

import animation.anime.dto.AnimeResponse;

import java.util.List;

public record CharacterAnimesResponse(
        Long characterId,
        List<AnimeResponse> animeResponseList
) {
}
