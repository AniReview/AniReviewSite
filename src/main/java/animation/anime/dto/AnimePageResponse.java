package animation.anime.dto;

import java.util.List;

// 메인페이지 10개 페이징, 조회페이지 20개 페이징
public record AnimePageResponse(
        List<AnimeResponse> responseList
) {
}
