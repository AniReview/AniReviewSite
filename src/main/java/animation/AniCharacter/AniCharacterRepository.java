package animation.AniCharacter;

import animation.anime.Anime;
import animation.character.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AniCharacterRepository extends JpaRepository<AniCharacter, Long> {
    boolean existsByAnimeAndCharacter(Anime anime, Character character);
}
