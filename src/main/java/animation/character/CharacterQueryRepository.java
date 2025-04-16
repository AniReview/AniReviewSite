package animation.character;

import animation.character.dto.CharacterResponse;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CharacterQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QCharacter character = QCharacter.character;

    public CharacterQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public List<CharacterResponse> findAll(
            OrderBy orderBy,
            Pageable pageable) {

        return jpaQueryFactory
                .selectFrom(character)
                .where(character.isDeleted.eq(false))
                .orderBy(getOrderSpecifier(orderBy))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(c -> new CharacterResponse(c.getId(), c.getName(), c.getImageUrl(), c.getFavoriteCount()))
                .toList();

    }

    private OrderSpecifier<?> getOrderSpecifier(OrderBy orderBy) {
        return switch (orderBy) {
            case ALPHABETICAL -> character.name.asc();
            case POPULAR -> character.favoriteCount.desc();
        };
    }


    public long countFiltered(OrderBy orderBy, Pageable pageable) {
        Long count = jpaQueryFactory
                .select(character.count())
                .from(character)
                .where(character.isDeleted.eq(false))
                .fetchOne();

        return count != null ? count : 0L;

    }
}
