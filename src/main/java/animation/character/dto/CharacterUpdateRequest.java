package animation.character.dto;

public record CharacterUpdateRequest(
        String charName,
        String charImageUrl,
        String charAbout
) {
}
