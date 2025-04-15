package animation.character.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JpgImage(
        @JsonProperty("image_url")
        String imageUrl
) {}
