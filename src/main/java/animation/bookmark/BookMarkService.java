package animation.bookmark;

import animation.anime.Anime;
import animation.anime.AnimeRepository;
import animation.bookmark.dto.*;
import animation.member.Member;
import animation.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final MemberRepository memberRepository;
    private final AnimeRepository animeRepository;
    private final BookMarkQueryRepository bookMarkQueryRepository;

    public BookMarkResponse create(String memberLoginId, BookmarkRequest bookmarkRequest) {
        Member member = findByLoginId(memberLoginId);

        Anime anime = animeRepository.findById(bookmarkRequest.aniId())
                .orElseThrow(() -> new NoSuchElementException("해당 애니메이션이 없습니다."));

        if(bookMarkRepository.existsByMemberIdAndAnimeId(member.getId(), anime.getId())){
            throw new NoSuchElementException("이미 북마크를 했습니다.");
        }

        bookMarkRepository.save(new BookMark(anime, member));

        return new BookMarkResponse(
                anime.getId(),
                member.getId(),
                LocalDateTime.now()
        );
    }

    public BookMarkPageResponse readAll(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("등록되지 않은 회원입니다."));

        return bookMarkQueryRepository.getBookmarksByMemberId(memberId, pageable);
    }

    @Transactional
    public BookMarkDeleteResponse delete(Long bookmarkId, String memberToken) {
        Member member = findByLoginId(memberToken);

        BookMark bookMark = bookMarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new NoSuchElementException("해당 북마크가 없습니다."));

        if(bookMark.getMember() == null || !bookMark.getMember().getId().equals(member.getId())){
            throw new NoSuchElementException("삭제할 권한이 없습니다.");
        }

        bookMarkRepository.deleteById(bookmarkId);

        return new BookMarkDeleteResponse(
                bookMark.getAnime().getTitle() + "의 북마크를 삭제했습니다.",
                new BookMarkDeleteData(
                        bookMark.getAnime().getId(),
                        bookmarkId,
                        member.getId()
                )
        );
    }

    private Member findByLoginId(String memberToken){
        return memberRepository.findByLoginId(memberToken)
                .orElseThrow(() -> new NoSuchElementException("등록되지 않은 회원입니다."));
    }
}
