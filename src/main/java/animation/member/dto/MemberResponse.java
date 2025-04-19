package animation.member.dto;

import java.time.LocalDate;

public record MemberResponse(
        Long id,
        String loginId,
        String myChar,
        LocalDate birth,
        String imageUrl
) {
}
