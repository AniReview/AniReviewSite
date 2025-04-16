package animation.character.dto;

public record CharacterDetailResponse(
        Long characterId,
        String charName,
        String imageUrl,
        String charAbout
) {
}
