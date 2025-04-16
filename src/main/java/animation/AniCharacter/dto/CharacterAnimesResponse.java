package animation.AniCharacter.dto;

public record CharacterAnimesResponse(
        Long characterId,
        Long animeId,
        String imageUrl,
        String title
) {
}
