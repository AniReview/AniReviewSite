package animation.member.dto;

import java.time.LocalDate;

public record MemberDetailResponse(Long id,
                                   String nickName,
                                   String myChar,
                                   LocalDate birth,
                                   String myUrl,
                                   Integer friendCount,
                                   String IntroduceCount) {
}
