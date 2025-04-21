package animation.bookmark;

import animation.anime.Anime;
import animation.anime.AnimeRepository;
import animation.bookmark.dto.BookMarkDeleteResponse;
import animation.bookmark.dto.BookMarkResponse;
import animation.bookmark.dto.BookmarkReadResponse;
import animation.bookmark.dto.BookmarkRequest;
import animation.character.Character;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class bookmarkUnitTest {

    @Mock
    private BookMarkRepository bookMarkRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AnimeRepository animeRepository;

    @InjectMocks
    private BookMarkService bookMarkService;

    private Member testMember;
    private Anime testAnime;
    private BookMark testBookMark;
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
        testBookMark = new BookMark(testAnime, testMember);
        ReflectionTestUtils.setField(testBookMark, "id", 1L);
        ReflectionTestUtils.setField(testMember, "id", 1L);
        ReflectionTestUtils.setField(testAnime, "id", 2L);
    }

    @Test
    @DisplayName("북마크 성공")
    void 북마크_성공() {
        BookmarkRequest request = new BookmarkRequest(testAnime.getId());

        when(memberRepository.findByLoginId(testMember.getLoginId())).thenReturn(Optional.of(testMember));
        when(animeRepository.findById(testAnime.getId())).thenReturn(Optional.of(testAnime));
        when(bookMarkRepository.existsByMemberIdAndAnimeId(testMember.getId(), testAnime.getId())).thenReturn(false);

        // when
        BookMarkResponse response = bookMarkService.create(testMember.getLoginId(), request);

        // then
        assertThat(response.aniId()).isEqualTo(testAnime.getId());
        assertThat(response.memberId()).isEqualTo(testMember.getId());
    }

    @Test
    @DisplayName("북마크 중복 생성 실패")
    void 북마크_중복_생성_실패() {
        // given
        BookmarkRequest request = new BookmarkRequest(testAnime.getId());
        when(memberRepository.findByLoginId(testMember.getLoginId())).thenReturn(Optional.of(testMember));
        when(animeRepository.findById(testAnime.getId())).thenReturn(Optional.of(testAnime));
        when(bookMarkRepository.existsByMemberIdAndAnimeId(testMember.getId(), testAnime.getId())).thenReturn(true);

        // when & then
        assertThrows(NoSuchElementException.class,
                () -> bookMarkService.create(testMember.getLoginId(), request));
    }

    @Test
    @DisplayName("북마크 조회 성공")
    void 북마크_조회() {
        BookmarkRequest request = new BookmarkRequest(testAnime.getId());

        when(memberRepository.findById(testMember.getId())).thenReturn(Optional.of(testMember));
        when(bookMarkRepository.findByMemberId(testMember.getId())).thenReturn(List.of(testBookMark));

        BookmarkReadResponse readAll = bookMarkService.readAll(testMember.getId());

        assertThat(readAll.memberId()).isEqualTo(testMember.getId());
        assertThat(readAll.bookmarks()).hasSize(1);
        assertThat(readAll.bookmarks().get(0).aniId()).isEqualTo(testAnime.getId());
    }

    @Test
    @DisplayName("북마크 삭제 성공")
    void 북마크_삭제_성공() {
        // given
        when(memberRepository.findByLoginId(testMember.getLoginId())).thenReturn(Optional.of(testMember));
        when(bookMarkRepository.findById(testBookMark.getId())).thenReturn(Optional.of(testBookMark));

        // when
        BookMarkDeleteResponse response = bookMarkService.delete(testBookMark.getId(), testMember.getLoginId());

        // then
        assertThat(response.message()).contains("삭제했습니다.");
        assertThat(response.bookMarkDeleteData().bookmarkId()).isEqualTo(testBookMark.getId());
        verify(bookMarkRepository).deleteById(testBookMark.getId());
    }

    @Test
    @DisplayName("북마크 삭제 권한 없음 실패")
    void 북마크_삭제_권한_없음_실패() {
        // given
        Member otherMember = new Member("loginId", "123", "nickname", testCharacter, LocalDate.of(2000, 1, 1), "url" );
        ReflectionTestUtils.setField(otherMember, "id", 2L);

        when(memberRepository.findByLoginId(testMember.getLoginId())).thenReturn(Optional.of(otherMember));
        when(bookMarkRepository.findById(testBookMark.getId())).thenReturn(Optional.of(testBookMark));

        // when & then
        assertThrows(NoSuchElementException.class,
                () -> bookMarkService.delete(testBookMark.getId(), testMember.getLoginId()));
    }
}
