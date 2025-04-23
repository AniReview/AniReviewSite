package animation.member;

import animation.character.QCharacter;
import animation.member.dto.MemberSimpleDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QMember member = QMember.member;
    private final QCharacter character = QCharacter.character;

    public MemberQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<MemberSimpleDto> findAll(Pageable pageable, String keyWord) {

        // map을 쓰지 않고 필요한 필드만 조회하여 가져오는 방식 , 캐릭터에 대해 n + 1 발생하지 않았음 포스트맨 테스트
        return jpaQueryFactory
                .select(Projections.constructor(MemberSimpleDto.class,
                        member.id,
                        member.nickName,
                        character.name,
                        member.imageUrl))
                .from(member)
                // character가 null일 경우 조회되지 않는 현상을 방지하기 위해 left join사용
                .leftJoin(member.character, character)
                .where(member.isDeleted.eq(false),
                        findByKeyWard(keyWord))
                .orderBy(member.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression findByKeyWard(String keyWard) {
        if (keyWard == null) {
            return null;
        }
        return member.nickName.contains(keyWard);
    }
}
