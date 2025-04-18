package animation.AniCharacter;

import animation.AniCharacter.dto.AnimeCharactersResponse;
import animation.AniCharacter.dto.CharacterAnimesResponse;
import animation.anime.QAnime;
import animation.anime.dto.AnimeResponse;
import animation.character.QCharacter;
import animation.character.dto.CharacterResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AniCharacterQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QAniCharacter aniCharacter = QAniCharacter.aniCharacter;
    private final QCharacter character = QCharacter.character;
    private final QAnime anime = QAnime.anime;

    public List<AnimeCharactersResponse> findByAnimeId(Long animeId){
        List<CharacterResponse> characterResponses = jpaQueryFactory
                .select(Projections.constructor(CharacterResponse.class,
                        character.id,
                        character.name,
                        character.imageUrl,
                        character.favoriteCount
                ))
                .from(aniCharacter)
                .join(aniCharacter.character, character)
                .where(aniCharacter.anime.id.eq(animeId))
                .fetch();

        return List.of(new AnimeCharactersResponse(animeId,characterResponses));
    }

    public List<CharacterAnimesResponse> findByCharId(Long characterId) {
        List<AnimeResponse> animeResponses = jpaQueryFactory
                .select(Projections.constructor(AnimeResponse.class,
                        anime.id,
                        anime.imageUrl,
                        anime.title,
                        anime.bookmark))
                .from(aniCharacter)
                .join(aniCharacter.anime, anime)
                .where(aniCharacter.character.id.eq(characterId))
                .fetch();

        return List.of(new CharacterAnimesResponse(characterId,animeResponses));
    }
}
