package animation.anime;

import animation.anime.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @PostMapping("/animes/{malId}")
    public AnimeCreateResponse importAnime(@PathVariable Long malId) {
        return animeService.importAnimeById(malId);
    }

    @GetMapping("/animes")
    public AnimePageResponse findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "ALL") AnimeFilter airing) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return animeService.findAll(pageable, airing);
    }

    @GetMapping("animes/{animeId}")
    public AnimeDetailResponse findById(@PathVariable Long animeId) {
        return animeService.findById(animeId);
    }

    @DeleteMapping("/animes/{animeId}")
    public AnimeStatus delete(@PathVariable Long animeId) {
        return animeService.delete(animeId);
    }
}


