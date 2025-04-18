package animation.AniCharacter;

import animation.anime.Anime;
import animation.character.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AniCharacterRepository extends JpaRepository<AniCharacter, Long> {
    List<AniCharacter> findByAnime_Id(Long animeId);

    boolean existsByAnimeAndCharacter(Anime anime, Character character);

    List<AniCharacter> findByCharacter_Id(Long characterId);
}
