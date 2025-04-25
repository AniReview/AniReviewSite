package animation.friendRequest;

import animation.loginUtils.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FriendRequestRestController {

    private final FriendRequestService requestService;

    // 권장되는 방식 (하이픈 사용)
    @PostMapping("/friend-request")
    public FriendRequestResponseDto create(@LoginMember String auth, @RequestBody FriendRequestDto request) {
        return requestService.create(auth, request);
    }
}
