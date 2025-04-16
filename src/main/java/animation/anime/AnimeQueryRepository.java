package animation.anime;

import animation.character.QCharacter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class AnimeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QAnime qAnime = QAnime.anime;

    public AnimeQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }
}
