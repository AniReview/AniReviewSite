package animation.character.dto;

public record CharacterResponse(
        Long characterId,
        String charName,
        String imageUrl,
        int favoriteCount

) {
}
