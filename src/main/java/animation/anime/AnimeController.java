package animation.anime;

import animation.anime.dto.AnimeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @PostMapping("/anime/{malId}")
    public AnimeResponse importAnime(@PathVariable Long malId) {
         return animeService.importAnimeById(malId);
    }
}


