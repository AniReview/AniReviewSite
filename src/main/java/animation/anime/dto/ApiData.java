package animation.anime.dto;

import java.util.List;

public record ApiData(Long mal_id,
                      String title,
                      String type,
                      String images,
                      Integer episodes,
                      String rating,
                      Integer aired,
                      String synopsis,
                      List<String> genres,
                      List<String> studios,
                      String duration,
                      boolean airing) {
}
