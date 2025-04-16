package animation.AniCharacter.dto;

public record AnimeCharactersResponse(
        Long animeId,
        Long characterId,
        String imageUrl,
        String name
) {
}
