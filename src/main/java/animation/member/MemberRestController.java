package animation.member;

import animation.member.dto.MemberCreateRequest;
import animation.member.dto.MemberCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberRestController {

    private final MemberService memberService;


    @PostMapping("/members")
    public MemberCreateResponse create(@RequestBody MemberCreateRequest memberCreateRequest) {
        return memberService.create(memberCreateRequest);
    }

}
