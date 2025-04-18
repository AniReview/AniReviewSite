package animation.character;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Character c SET c.favoriteCount = c.favoriteCount + 1 WHERE c.id = :id")
    int increaseFavoriteCountById(Long id);
}
