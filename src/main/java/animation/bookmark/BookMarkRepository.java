package animation.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    boolean existsByMemberIdAndAnimeId(Long memberId, Long animeId);

    List<BookMark> findByMemberId(Long memberId);
}
