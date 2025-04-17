package animation.AniCharacter.dto;

import animation.character.dto.CharacterResponse;

import java.util.List;

public record AnimeCharactersResponse(
        Long animeId,
        List<CharacterResponse> characterResponses
) {
}
