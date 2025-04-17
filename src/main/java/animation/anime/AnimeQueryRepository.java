package animation.anime;

import animation.anime.dto.AnimeResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnimeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QAnime anime = QAnime.anime;

    public AnimeQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<AnimeResponse> findAll(Pageable pageable, AnimeFilter airing) {
        return jpaQueryFactory
                .selectFrom(anime)
                .where(anime.isDeleted.eq(false), animeFilter(airing))
                .orderBy(anime.bookmark.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream().map(a -> new AnimeResponse(a.getId(), a.getImageUrl(), a.getTitle(),a.getBookmark())).toList();
    }

    private BooleanExpression animeFilter(AnimeFilter airing) {
        if (airing == AnimeFilter.AIRING) {
            return anime.airing.eq(true);
        }
        return null;
    }
}
