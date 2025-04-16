package animation.anime;

import animation.anime.dto.AnimeCreateResponse;
import animation.anime.dto.AnimePageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @PostMapping("/animes/{malId}")
    public AnimeCreateResponse importAnime(@PathVariable Long malId) {
         return animeService.importAnimeById(malId);
    }

    @GetMapping("/animes")
    public AnimePageResponse findAll() {
        return animeService.findAll();
    }
}


