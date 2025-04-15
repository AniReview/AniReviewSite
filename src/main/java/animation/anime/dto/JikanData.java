package animation.anime.dto;

import java.util.List;

// API 데이터 중 필요한 부분만 매핑
public record JikanData(
        Long mal_id,
        String title,
        String type,
        JikanImages images,
        Integer episodes,
        String rating,
        JikanAired aired,
        String synopsis,
        List<JikanGenre> genres,
        List<JikanStudio> studios,
        String duration
) {}
