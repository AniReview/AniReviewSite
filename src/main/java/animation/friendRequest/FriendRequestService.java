package animation.friendRequest;

import animation.SelfFriendRequestException;
import animation.member.Member;
import animation.member.MemberRepository;
import animation.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

import static animation.friendRequest.Status.PENDING;

@RequiredArgsConstructor
@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    private void validateNotSelf(Member requester, Member receiver) {
        if (Objects.equals(requester.getId(), receiver.getId())) {
            throw new SelfFriendRequestException("자기 자신에게는 친구 요청을 보낼 수 없습니다.");
        }
    }

    public FriendRequestResponseDto create(String loginId, FriendRequestDto request) {
        Member requestMember = memberService.findByLoginId(loginId);

        Member receiverMember = memberRepository.findById(request.receiverMemberId()).orElseThrow(() ->
                new NoSuchElementException("존재하지 않는 멤버 : " + request.receiverMemberId()));

        validateNotSelf(requestMember, receiverMember);

        friendRequestRepository.save(new FriendRequest(requestMember, receiverMember, PENDING));

        return new FriendRequestResponseDto(requestMember.getId(), receiverMember.getId());
    }
}
