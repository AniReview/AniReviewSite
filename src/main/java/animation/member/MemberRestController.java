package animation.member;

import animation.S3.S3Service;
import animation.admin.dto.AdminCreate;
import animation.member.dto.MemberCreateRequest;
import animation.member.dto.MemberCreateResponse;
import animation.member.dto.MemberLoginRequest;
import animation.member.dto.MemberLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class MemberRestController {

    private final MemberService memberService;
    private final S3Service s3Service;


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

}
