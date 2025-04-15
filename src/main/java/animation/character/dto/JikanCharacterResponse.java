package animation.character.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JikanCharacterResponse(
        CharacterData data
) {}