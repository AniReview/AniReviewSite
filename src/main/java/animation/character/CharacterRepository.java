package animation.character;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {

    Optional<Character> findByIdAndIsDeletedFalse(Long id);
    Optional<Character> findByMalId(Long malId);

    @Modifying
    @Transactional
    @Query("UPDATE Character c SET c.favoriteCount = c.favoriteCount + 1 WHERE c.id = :id")
    int increaseFavoriteCountById(Long id);
}
