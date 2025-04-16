package animation.AniCharacter;

import animation.AniCharacter.dto.AnimeCharactersResponse;
import animation.AniCharacter.dto.CharacterAnimesResponse;
import animation.AniCharacter.dto.JikanCharacterListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<AnimeCharactersResponse> getAnimeCharacters(@PathVariable Long animeId){
        return aniCharacterService.getAnimeCharacters(animeId);
    }

    @GetMapping("/anicharacter/character/{characterId}")
    public List<CharacterAnimesResponse> getCharacterAnimes(@PathVariable Long characterId){
        return aniCharacterService.getCharacterAnimes(characterId);
    }


}
