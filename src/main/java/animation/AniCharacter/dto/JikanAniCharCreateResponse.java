package animation.AniCharacter.dto;

public record JikanAniCharCreateResponse(
        Long aniCharacterId,
        Long animeId,
        Long characterId
) {
}
