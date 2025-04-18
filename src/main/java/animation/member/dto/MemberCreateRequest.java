package animation.member.dto;

import java.time.LocalDate;

public record MemberCreateRequest(
        String loginId,
        String password,
        String nickName,
        Long charId,
        LocalDate birth,
        String imageUrl) {
}
