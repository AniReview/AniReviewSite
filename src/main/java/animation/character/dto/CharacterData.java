package animation.character.dto;

public record CharacterData(
        Long mal_id,
        String name,
        String about,
        ImageGroup images
) {}