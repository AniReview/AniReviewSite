package animation.character.dto;

public record CharacterUpdateResponse(
        Long characterId,
        String charName,
        String charImageUrl,
        String charAbout
) {
}
