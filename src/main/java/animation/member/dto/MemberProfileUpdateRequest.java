package animation.member.dto;

import java.time.LocalDate;

public record MemberProfileUpdateRequest(String nickName, LocalDate birth, String introduce) {
}
