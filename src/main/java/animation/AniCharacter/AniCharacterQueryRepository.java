package animation.AniCharacter;

import animation.AniCharacter.dto.AniCharPageResponse;
import animation.AniCharacter.dto.AnimeCharactersResponse;
import animation.AniCharacter.dto.CharAniPageResponse;
import animation.AniCharacter.dto.CharacterAnimesResponse;
import animation.anime.QAnime;
import animation.anime.dto.AnimeResponse;
import animation.character.QCharacter;
import animation.character.dto.CharacterResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AniCharacterQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QAniCharacter aniCharacter = QAniCharacter.aniCharacter;
    private final QCharacter character = QCharacter.character;
    private final QAnime anime = QAnime.anime;

    public AniCharPageResponse findByAnimeId(Long animeId, Pageable pageable){

        //페이징 : 애니속 캐릭터니까 애니id 저장
        List<Long> aniCharIds = jpaQueryFactory.select(aniCharacter.id)
                .from(aniCharacter)
                .where(aniCharacter.anime.id.eq(animeId))
                .orderBy(character.malId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if(aniCharIds.isEmpty()){
            return new AniCharPageResponse(0, 1L, pageable.getPageNumber() + 1, pageable.getPageSize(), List.of());
        }

        //조인
        List<CharacterResponse> characterResponses = jpaQueryFactory
                .select(Projections.constructor(CharacterResponse.class,
                        character.id,
                        character.name,
                        character.imageUrl,
                        character.favoriteCount
                ))
                .from(aniCharacter)
                .join(aniCharacter.character, character)
                .where(aniCharacter.anime.id.in(aniCharIds))
                .orderBy(character.malId.desc())
                .fetch();

        long totalCount = aniCharCountFiltered(animeId);
        int totalPage = (int) Math.ceil((double) totalCount / pageable.getPageSize());
        int currentPage = pageable.getPageNumber() + 1;

        return new AniCharPageResponse(
                totalPage,
                totalCount,
                currentPage,
                pageable.getPageSize(),
                List.of(new AnimeCharactersResponse(animeId, characterResponses)));
    }

    public CharAniPageResponse findByCharId(Long characterId, Pageable pageable) {

        List<Long> charIds = jpaQueryFactory.select(aniCharacter.id)
                .from(aniCharacter)
                .where(aniCharacter.character.id.eq(characterId))
                .orderBy(character.malId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<AnimeResponse> animeResponses = jpaQueryFactory
                .select(Projections.constructor(AnimeResponse.class,
                        anime.id,
                        anime.imageUrl,
                        anime.title,
                        anime.bookmark))
                .from(aniCharacter)
                .join(aniCharacter.anime, anime)
                .where(aniCharacter.character.id.in(charIds))
                .orderBy(character.malId.desc())
                .fetch();

        long totalCount = CharAniCountFiltered(characterId);
        int totalPage = (int) Math.ceil((double) totalCount / pageable.getPageSize());
        int currentPage = pageable.getPageNumber() + 1;

        return new CharAniPageResponse(
                totalPage,
                totalCount,
                currentPage,
                pageable.getPageSize(),
                List.of(new CharacterAnimesResponse(characterId, animeResponses))
        );
    }

    private long aniCharCountFiltered(Long aniId) {
        Long count = jpaQueryFactory.select(aniCharacter.count())
                .from(aniCharacter)
                .join(aniCharacter.anime, anime)
                .where(aniCharacter.anime.id.eq(aniId))
                .fetchOne();

        return count != null ? count : 0L;
    }

    private long CharAniCountFiltered(Long charId) {
        Long count = jpaQueryFactory.select(aniCharacter.count())
                .from(aniCharacter)
                .join(aniCharacter.character, character)
                .where(aniCharacter.character.id.eq(charId))
                .fetchOne();

        return count != null ? count : 0L;
    }
}
