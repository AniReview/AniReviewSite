package animation.bookmark;

import animation.anime.QAnime;
import animation.bookmark.dto.BookMarkPageResponse;
import animation.bookmark.dto.BookMarkResponse;
import animation.bookmark.dto.BookmarkData;
import animation.bookmark.dto.BookmarkReadResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookMarkQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QBookMark bookMark = QBookMark.bookMark;
    private final QAnime anime = QAnime.anime;

    public BookMarkPageResponse getBookmarksByMemberId(Long memberId, Pageable pageable){

        //페이징해서 필요한 bookmarkId를 가져오기
        List<Long> bookmarkIds = jpaQueryFactory.select(bookMark.id)
                .from(bookMark)
                .where(bookMark.member.id.eq(memberId))
                .orderBy(bookMark.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if(bookmarkIds.isEmpty()){
            return new BookMarkPageResponse(0, 0L, 0, 0,
                    new BookmarkReadResponse(memberId, List.of()));
        }

        //id기준으로 애니와 join
        List<BookmarkData> data = jpaQueryFactory.select(Projections.constructor(BookmarkData.class,
                        anime.id,
                        anime.title,
                        anime.imageUrl))
                .from(bookMark)
                .join(bookMark.anime, anime)
                .where(bookMark.id.in(bookmarkIds), anime.isDeleted.isFalse())
                .orderBy(anime.id.desc())
                .fetch();

        //전체 페이징
        Long totalCount = countFiltered(memberId);

        // 페이징 정보 계산
        int totalPage = (int) Math.ceil((double) totalCount / pageable.getPageSize());
        int currentPage = pageable.getPageNumber() + 1;

        return new BookMarkPageResponse(
                totalPage,
                totalCount,
                currentPage,
                pageable.getPageSize(),
                new BookmarkReadResponse(memberId, data)
        );

    }

    public long countFiltered(Long memberId) {
        Long count = jpaQueryFactory.select(bookMark.count())
                .from(bookMark)
                .join(bookMark.anime, anime)
                .where(bookMark.member.id.eq(memberId), anime.isDeleted.isFalse())
                .fetchOne();

        return count != null ? count : 0L;
    }
}
