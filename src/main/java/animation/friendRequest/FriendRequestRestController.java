package animation.friendRequest;

import animation.friendRequest.dto.*;
import animation.loginUtils.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class FriendRequestRestController {

    private final FriendRequestService requestService;

    // 권장되는 방식 (하이픈 사용)
    // 중복 요청 못하도록 구현해야함
    @PostMapping("/friend-request")
    public FriendRequestResponseDto create(@LoginMember String auth, @RequestBody FriendRequestDto request) {
        return requestService.create(auth, request);
    }

    // 지금 목록 조회가 이상함
    // 요청받는 회원의 요청한 멤버리스트가 나오도록 수정해야함
    @GetMapping("/friend-request")
    public FrListResponse findAll(@LoginMember String auth) {
        return requestService.findAll(auth);
    }

    @PatchMapping("/friend-request/{friendRequestId}")
    public FrStatusResponse frDecision(@LoginMember String auth, @PathVariable Long friendRequestId, @RequestBody Status status) {
        return requestService.frDecision(auth,friendRequestId,status);
    }
}
