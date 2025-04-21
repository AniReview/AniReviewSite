package animation.loveAni;

import animation.anime.Anime;
import animation.anime.AnimeRepository;
import animation.character.Character;
import animation.loveAni.dto.DeleteLoveAniResponse;
import animation.loveAni.dto.LoveRequest;
import animation.loveAni.dto.LoveResponse;
import animation.member.Member;
import animation.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class loveAniTest {

    @Mock
    private LoveAniRepository loveAniRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AnimeRepository animeRepository;

    @InjectMocks
    private LoveAniService loveAniService;

    private Member testMember;
    private Anime testAnime;
    private LoveAni testLoveAni;
    private Character testCharacter;

    @BeforeEach
    void setUp() {
        List<String> genres = new ArrayList<>();
        genres.add("Action");
        genres.add("Adventure");

        testAnime = new Anime(
                "testTitle",                     // title
                "https://example.com/image.jpg", // imageUrl
                "TV",                            // type
                "Director Name",                 // supervision
                genres,                          // genres
                24,                              // episodes
                "PG-13",                         // rating
                LocalDateTime.of(2023, 4, 1, 0, 0), // aired
                "This is a test synopsis for the anime.", // synopsis
                "Studio Name",                   // productionCompany
                "24 min per episode",            // duration
                false,                           // airing
                123456L                          // malId
        );
        testCharacter = new animation.character.Character("name", "url", "about", 12L);
        testMember = new Member("loginId", "123", "nickname", testCharacter, LocalDate.of(2000, 1, 1), "url" );
        testLoveAni = new LoveAni(testMember, testAnime);
        ReflectionTestUtils.setField(testLoveAni, "id", 1L);
        ReflectionTestUtils.setField(testMember, "id", 1L);
        ReflectionTestUtils.setField(testAnime, "id", 2L);
    }

    @Test
    @DisplayName("좋아요 생성 성공")
    void 좋아요생성() {
        LoveRequest request = new LoveRequest(testAnime.getId());

        //Optional.of = null이 아닌 testMember를 return해라 / null이면 에러
        when(memberRepository.findByLoginId(testMember.getLoginId())).thenReturn(Optional.of(testMember));
        when(animeRepository.findById(testAnime.getId())).thenReturn(Optional.of(testAnime));
        when(loveAniRepository.existsByMemberIdAndAnimeId(testMember.getId(), testAnime.getId())).thenReturn(false);
        when(loveAniRepository.save(any(LoveAni.class))).thenReturn(testLoveAni);

        LoveResponse response = loveAniService.create(testMember.getLoginId(), request);

        assertEquals(testAnime.getId(), response.aniId());
        assertEquals(testMember.getId(), response.memberId());
        assertThat(response.loveId()).isEqualTo(testLoveAni.getId());
        assertThat(response.loveId()).isNotNull();
    }

    @Test
    @DisplayName("좋아요 이미 존재 시 예외 발생")
    void 좋아요생성_실패1() {
        LoveRequest request = new LoveRequest(testAnime.getId());

        when(memberRepository.findByLoginId(testMember.getLoginId())).thenReturn(Optional.of(testMember));
        when(animeRepository.findById(testAnime.getId())).thenReturn(Optional.of(testAnime));
        when(loveAniRepository.existsByMemberIdAndAnimeId(testMember.getId(), testAnime.getId())).thenReturn(true);

        assertThrows(NoSuchElementException.class, () -> loveAniService.create(testMember.getLoginId(), request));
    }

    @Test
    @DisplayName("좋아요 삭제 성공")
    void 좋아요_삭제_성공() {
        LoveRequest request = new LoveRequest(testAnime.getId());

        when(memberRepository.findByLoginId(testMember.getLoginId())).thenReturn(Optional.of(testMember));
        when(loveAniRepository.findById(testLoveAni.getId())).thenReturn(Optional.of(testLoveAni));
        when(animeRepository.findById(testAnime.getId())).thenReturn(Optional.of(testAnime));

        DeleteLoveAniResponse deleted = loveAniService.delete(testMember.getLoginId(), testLoveAni.getId());

        assertEquals("좋아요가 삭제되었습니다.", deleted.message());
        assertEquals(testAnime.getId(), deleted.dataResponse().aniId());
        assertEquals(testLoveAni.getId(), deleted.dataResponse().loveId());
        assertEquals(testMember.getId(), deleted.dataResponse().memberId());

        verify(loveAniRepository).deleteById(deleted.dataResponse().loveId());
    }

    @Test
    @DisplayName("좋아요가 존재하지 않으면 예외발생")
    void 좋아요_삭제_실패1() {
        when(memberRepository.findByLoginId(testMember.getLoginId())).thenReturn(Optional.of(testMember));
        when(loveAniRepository.findById(testLoveAni.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> loveAniService.delete(testMember.getLoginId(), testLoveAni.getId()));
    }

    @Test
    @DisplayName("권한이 없으면 좋아요 삭제 실패")
    void 좋아요_삭제_실패2() {
        Member testMember2 = new Member("loginId", "123", "nickname", testCharacter, LocalDate.of(2000, 1, 1), "url" );
        ReflectionTestUtils.setField(testMember2, "id", 999L);

        when(animeRepository.findById(testAnime.getId())).thenReturn(Optional.of(testAnime));
        when(memberRepository.findByLoginId(testMember2.getLoginId())).thenReturn(Optional.of(testMember2));
        when(loveAniRepository.findById(testLoveAni.getId())).thenReturn(Optional.of(testLoveAni));

        assertThrows(NoSuchElementException.class, () -> loveAniService.delete(testMember2.getLoginId(), testLoveAni.getId()));

    }
}
