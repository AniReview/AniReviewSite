package animation.loveAni;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoveAniRepository extends JpaRepository<LoveAni, Long> {

    boolean existsByMemberIdAndAnimeId(Long memberId, Long animeId);
}
