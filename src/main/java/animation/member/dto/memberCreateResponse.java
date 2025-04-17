package animation.member.dto;

import java.time.LocalDate;

public record memberCreateResponse(
        Long id,
        String loginId,
        String myChar,
        LocalDate birth,
        String imageUrl
) {
}
