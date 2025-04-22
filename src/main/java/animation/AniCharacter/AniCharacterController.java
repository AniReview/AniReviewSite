package animation.AniCharacter;

import animation.AniCharacter.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AniCharacterController {

    private final AniCharacterService aniCharacterService;

    //Todo: 관리자 토큰 넣기
    @PostMapping("/animes/{animeMalId}/characters")
    public JikanCharacterListResponse create(@PathVariable Long animeMalId){
        return  aniCharacterService.saveAniCharById(animeMalId);
    }

    @GetMapping("/anicharacter/anime/{animeId}")
    public AniCharPageResponse getAnimeCharacters(@PathVariable Long animeId,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return aniCharacterService.getAnimeCharacters(animeId,pageable);
    }

    @GetMapping("/anicharacter/character/{characterId}")
    public CharAniPageResponse getCharacterAnimes(@PathVariable Long characterId,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        return aniCharacterService.getCharacterAnimes(characterId, pageable);
    }


}
