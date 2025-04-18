package animation.member.dto;

import java.time.LocalDate;

public record MemberCreateResponse(
        Long id,
        String loginId,
        String myChar,
        LocalDate birth,
        String imageUrl
) {
}
