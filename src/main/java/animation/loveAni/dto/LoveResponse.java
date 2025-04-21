package animation.loveAni.dto;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public record LoveResponse(
        Long aniId,
        Long memberId,
        Long loveId,
        LocalDateTime createAt
) {
}
