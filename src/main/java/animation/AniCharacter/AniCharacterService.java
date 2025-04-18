package animation.AniCharacter;

import animation.AniCharacter.dto.*;
import animation.EmptyDataException;
import animation.anime.Anime;
import animation.anime.AnimeRepository;
import animation.anime.dto.AnimeResponse;
import animation.character.Character;
import animation.character.CharacterRepository;
import animation.character.CharacterService;
import animation.character.dto.CharacterResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class AniCharacterService {

    private final WebClient webClient;
    private final AniCharacterRepository aniCharacterRepository;
    private final AnimeRepository animeRepository;
    private final CharacterRepository characterRepository;
    private final AniCharacterQueryRepository aniCharacterQueryRepository;

    public AniCharacterService(WebClient.Builder builder, AniCharacterRepository aniCharacterRepository, AnimeRepository animeRepository, CharacterRepository characterRepository, AniCharacterQueryRepository aniCharacterQueryRepository) {
        this.aniCharacterRepository = aniCharacterRepository;
        this.webClient = builder.baseUrl("https://api.jikan.moe/v4").build();
        this.animeRepository = animeRepository;
        this.characterRepository = characterRepository;
        this.aniCharacterQueryRepository = aniCharacterQueryRepository;
    }

    @Transactional
    public JikanCharacterListResponse saveAniCharById(Long animeMalId) {
        Anime anime = animeRepository.findByMalId(animeMalId)
                .orElseThrow(() -> new NoSuchElementException("찾으시는 애니메이션이 없습니다."));

        // 관계 매핑 api호출
        JikanCharacterListResponse apiResponse = api(animeMalId);

        if (apiResponse == null || apiResponse.data() == null || apiResponse.data().isEmpty()) {
            throw new EmptyDataException("해당 애니메이션에 대한 캐릭터 데이터가 없습니다.");
        }

        List<JikanAniCharCreateResponse> result = new ArrayList<>();

        for (JikanCharacterData charData : apiResponse.data()) {
            Long charMalId = charData.character().mal_id();
            Character character = characterRepository.findByMalId(charMalId)
                    .orElseThrow(() -> new NoSuchElementException("해당 캐릭터는 db에 존재하지 않습니다."));

            if (aniCharacterRepository.existsByAnimeAndCharacter(anime, character)) {
                throw new IllegalStateException("이미 해당 애니와 캐릭터의 관계가 존재합니다.");
            }

            AniCharacter relation = aniCharacterRepository.save(new AniCharacter(character, anime));

            result.add(new JikanAniCharCreateResponse(
                        relation.getId(),
                        anime.getId(),
                        character.getId()
            ));

        }
        return apiResponse;
    }

    private JikanCharacterListResponse api(Long animeMalId) {
        return webClient.get()
                .uri("/anime/{id}/characters", animeMalId)
                .retrieve()
                .bodyToMono(JikanCharacterListResponse.class)
                .block();
    }

    public List<AnimeCharactersResponse> getAnimeCharacters(Long animeId) {
        return aniCharacterQueryRepository.findByAnimeId(animeId);
    }

    public List<CharacterAnimesResponse> getCharacterAnimes(Long characterId) {
        return aniCharacterQueryRepository.findByCharId(characterId);
    }

}
