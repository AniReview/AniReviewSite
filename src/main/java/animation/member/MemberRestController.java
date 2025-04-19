package animation.member;

import animation.S3.S3Service;
import animation.admin.dto.AdminCreate;
import animation.loginUtils.JwtProvider;
import animation.loginUtils.LoginMember;
import animation.loginUtils.LoginMemberResolver;
import animation.member.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class MemberRestController {

    private final MemberService memberService;
    private final S3Service s3Service;
    private final LoginMemberResolver loginMemberResolver;


    @PostMapping("/members")
    public MemberCreateResponse create(@RequestPart(value = "images") MultipartFile files,
                                        @RequestPart MemberCreateRequest memberCreateRequest) throws IOException {
        String url = s3Service.uploadFile(files);
        return memberService.create(url,memberCreateRequest);
    }

    @PostMapping("/members/login")
    public MemberLoginResponse login(@RequestBody MemberLoginRequest loginRequest) {
        return memberService.login(loginRequest);
    }

    @DeleteMapping("/members")
    public MemberDeleteResponse deleteMember(@LoginMember String auth) {
        return memberService.deleteMember(auth);
    }

}
