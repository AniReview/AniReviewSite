package animation.aniCharacter;

import animation.AniCharacter.AniCharacter;
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
import animation.character.CharacterService;
import animation.character.dto.CharacterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
                characterRepository
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

        assertThrows(NoSuchElementException.class, ()-> {
            aniCharacterService.saveAniCharById(1L);
        });
    }

    @Test
    @DisplayName("애니메이션 캐릭터 전체 조회")
    void getAnimeCharacters() throws Exception {
        // given
        Character character1 = new Character("짱구", "test1.jpg", "부리부리", 10L);
        Character character2 = new Character("철수", "test2.jpg", "철수책상철책상", 5L);

        // 리플렉션으로 ID 강제 주입
        Field idField = Character.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(character1, 1L); // character1의 ID를 1L로 설정
        idField.set(character2, 2L); // character2의 ID를 2L로 설정

        AniCharacter aniCharacter1 = new AniCharacter(character1, anime);
        AniCharacter aniCharacter2 = new AniCharacter(character2, anime);
        List<AniCharacter> mockAniCharacters = Arrays.asList(aniCharacter1, aniCharacter2);

        //애니 찾기
        when(aniCharacterRepository.findByAnime_Id(anime.getId())).thenReturn(mockAniCharacters);

        // when
        List<AnimeCharactersResponse> result = aniCharacterService.getAnimeCharacters(anime.getId());

        // then
        assertThat(result).hasSize(1);
        AnimeCharactersResponse response = result.get(0);
        assertThat(response.animeId()).isEqualTo(anime.getId());
        assertThat(response.characterResponses()).hasSize(2);

        // 첫 번째 캐릭터 검증
        CharacterResponse charRes1 = response.characterResponses().get(0);
        assertThat(charRes1.characterId()).isEqualTo(1L); // 리플렉션으로 설정한 ID
        assertThat(charRes1.charName()).isEqualTo("짱구");
        assertThat(charRes1.imageUrl()).isEqualTo("test1.jpg");

        // 두 번째 캐릭터 검증
        CharacterResponse charRes2 = response.characterResponses().get(1);
        assertThat(charRes2.characterId()).isEqualTo(2L); // 리플렉션으로 설정한 ID
        assertThat(charRes2.charName()).isEqualTo("철수");
        assertThat(charRes2.imageUrl()).isEqualTo("test2.jpg");
    }

    @Test
    @DisplayName("캐릭터 애니메이션 전체 조회")
    void getCharacterAnimes() throws Exception {
        // given
        Anime anime2 = new Anime(
                "강철의 연금술사: 브라더후드2", // title
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
                2L);

        // 리플렉션으로 ID 강제 주입
        Field idField = Anime.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(anime, 1L); // character1의 ID를 1L로 설정
        idField.set(anime2, 2L); // character2의 ID를 2L로 설정

        AniCharacter aniCharacter1 = new AniCharacter(character, anime);
        AniCharacter aniCharacter2 = new AniCharacter(character, anime2);
        List<AniCharacter> mockAniCharacters = Arrays.asList(aniCharacter1, aniCharacter2);

        //애니 찾기
        when(aniCharacterRepository.findByCharacter_Id(character.getId())).thenReturn(mockAniCharacters);

        // when
        List<CharacterAnimesResponse> result = aniCharacterService.getCharacterAnimes(character.getId());

        // then
        assertThat(result).hasSize(1);
        CharacterAnimesResponse response = result.get(0);
        assertThat(response.characterId()).isEqualTo(character.getId());
        assertThat(response.animeResponseList()).hasSize(2);

        // 첫 번째 애니 검증
        AnimeResponse aniRes1 = response.animeResponseList().get(0);
        assertThat(aniRes1.animeId()).isEqualTo(1L); // 리플렉션으로 설정한 ID
        assertThat(aniRes1.title()).isEqualTo("강철의 연금술사: 브라더후드");
        // 두 번째 애니 검증
        AnimeResponse aniRes2 = response.animeResponseList().get(1);
        assertThat(aniRes2.animeId()).isEqualTo(2L); // 리플렉션으로 설정한 ID
        assertThat(aniRes2.title()).isEqualTo("강철의 연금술사: 브라더후드2");
    }
}