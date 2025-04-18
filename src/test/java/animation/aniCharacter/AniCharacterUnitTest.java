package animation.aniCharacter;

import animation.AniCharacter.AniCharacterQueryRepository;
import animation.AniCharacter.AniCharacterRepository;
import animation.AniCharacter.AniCharacterService;
import animation.AniCharacter.dto.AnimeCharactersResponse;
import animation.AniCharacter.dto.CharacterAnimesResponse;
import animation.admin.AdminRepository;
import animation.anime.Anime;
import animation.anime.AnimeRepository;
import animation.anime.dto.AnimeResponse;
import animation.character.Character;
import animation.character.CharacterQueryRepository;
import animation.character.CharacterRepository;
import animation.character.dto.CharacterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;



public class AniCharacterUnitTest {

    @Mock
    private CharacterQueryRepository characterQueryRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private AnimeRepository animeRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private AniCharacterRepository aniCharacterRepository;

    @Mock
    private WebClient.Builder builder;

    @Mock
    private WebClient.Builder baseUrlBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    AniCharacterQueryRepository aniCharacterQueryRepository;

    private AniCharacterService aniCharacterService;
    private Character character;
    private Anime anime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // WebClient.Builder mock 설정
        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(webClient);

        aniCharacterService = new AniCharacterService(
                builder,
                aniCharacterRepository,
                animeRepository,
                characterRepository,
                aniCharacterQueryRepository
        );
        anime = new Anime(
                "강철의 연금술사: 브라더후드", // title
                "https://cdn.myanimelist.net/images/anime/1223/96541.jpg", // imageUrl
                "TV", // type
                "야스히로 이리", // supervision (감독)
                Arrays.asList("액션", "어드벤처", "판타지"), // genres
                64, // episodes
                "PG-13", // rating
                LocalDateTime.of(2009, 4, 5, 0, 0), // aired
                "연금술이 존재하는 세계에서 두 형제가 금지된 연성의 대가로 모든 것을 잃고, 잃어버린 것을 되찾기 위해 모험을 떠난다.", // synopsis
                "BONES", // productionCompany (studios)
                "24분", // duration
                false,
                1L
        );
        character = new Character("캐릭터", "image.jpg", "소개", 1L);
    }

    @Test
    @DisplayName("애니메이션이 존재하지 않으면 예외")
    void 예외1() {
        Long animeMalId = 100L;
        when(animeRepository.findByMalId(animeMalId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            aniCharacterService.saveAniCharById(1L);
        });
    }

    @Test
    @DisplayName("캐릭터가 db에 없으면 예외")
    void 예외2() {
        Long charMalId = 100L;
        when(characterRepository.findByMalId(charMalId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            aniCharacterService.saveAniCharById(1L);
        });
    }

    @Test
    @DisplayName("애니메이션 캐릭터 조회")
    void getAnimeCharacters() {
        List<CharacterResponse> characterResponses = List.of(
                new CharacterResponse(1L, "Character 1", "http://image1.com", 100),
                new CharacterResponse(2L, "Character 2", "http://image2.com", 200)
        );

        List<AnimeCharactersResponse> expectedResponse = List.of(new AnimeCharactersResponse(anime.getId(), characterResponses));

        when(aniCharacterQueryRepository.findByAnimeId(anime.getId())).thenReturn(expectedResponse);

        List<AnimeCharactersResponse> result = aniCharacterService.getAnimeCharacters(anime.getId());

        // 결과가 null이 아니고, 사이즈가 1이어야 한다.
        assertNotNull(result);
        assertEquals(1, result.size());

        AnimeCharactersResponse actualResponse = result.get(0);
        assertEquals(expectedResponse.get(0).animeId(), actualResponse.animeId());
        assertEquals(expectedResponse.get(0).characterResponses(), actualResponse.characterResponses());

        // findByAnimeId가 1번 호출되었는지 확인
        verify(aniCharacterQueryRepository, times(1)).findByAnimeId(anime.getId());
    }

    @Test
    @DisplayName("캐릭터 애니메이션 전체 조회")
    void getCharacterAnimes() throws Exception {
        List<AnimeResponse> animeResponses = List.of(
                new AnimeResponse(1L, "http://image1.com", "title1", 1),
                new AnimeResponse(2L, "http://image2.com", "title2",2)
        );

        List<CharacterAnimesResponse> expectedResponse = List.of(new CharacterAnimesResponse(character.getId(), animeResponses));

        when(aniCharacterQueryRepository.findByCharId(character.getId())).thenReturn(expectedResponse);

        List<CharacterAnimesResponse> result = aniCharacterService.getCharacterAnimes(character.getId());

        assertNotNull(result);
        assertEquals(1, result.size());

        CharacterAnimesResponse actualResponse = result.get(0);
        assertEquals(expectedResponse.get(0).characterId(), actualResponse.characterId());
        assertEquals(expectedResponse.get(0).animeResponseList(), actualResponse.animeResponseList());

        verify(aniCharacterQueryRepository, times(1)).findByCharId(anime.getId());

    }
}