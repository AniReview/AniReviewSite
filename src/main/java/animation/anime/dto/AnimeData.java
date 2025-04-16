package animation.anime.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AnimeData(
        String title,
        String type,
        String images,
        Integer episodes,
        String rating,
        LocalDateTime aired,
        String synopsis,
        List<String> genres,
        String studios,
        String duration,
        boolean airing,
        Long malId
) {
}
