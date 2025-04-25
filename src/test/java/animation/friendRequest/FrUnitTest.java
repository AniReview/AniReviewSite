package animation.friendRequest;

import animation.SelfFriendRequestException;
import animation.friendRequest.dto.FrListResponse;
import animation.friendRequest.dto.FrResponse;
import animation.friendRequest.dto.FriendRequestDto;
import animation.friendRequest.dto.FriendRequestResponseDto;
import animation.member.Member;
import animation.member.MemberRepository;
import animation.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FrUnitTest {

    @Mock
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @InjectMocks
    private FriendRequestService friendRequestService;

    private Member requester;
    private Member receiver;

    @BeforeEach
    void setUp() {
        requester = new Member("user1", "123", "nick1", null, LocalDate.of(2000, 1, 1), "url" ,"ㅎㅇㅎㅇ");
        ReflectionTestUtils.setField(requester, "id", 1L);
        receiver = new Member("user2", "123", "nick2", null, LocalDate.of(2000, 1, 1), "url" ,"ㅎㅇㅎㅇ");
        ReflectionTestUtils.setField(receiver, "id", 2L);
    }

    @Test
    @DisplayName("친구 요청 생성 성공")
    void createFriendRequestSuccess() {
        // given
        String loginId = "user1";
        FriendRequestDto requestDto = new FriendRequestDto(receiver.getId());

        when(memberService.findByLoginId(loginId)).thenReturn(requester);
        when(memberRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));

        // when
        FriendRequestResponseDto responseDto = friendRequestService.create(loginId, requestDto);

        // then
        verify(friendRequestRepository).save(any(FriendRequest.class));
        assertEquals(requester.getId(), responseDto.requestMemberId());
        assertEquals(receiver.getId(), responseDto.receiverMemberId());
    }

    @Test
    @DisplayName("자기 자신에게 친구 요청 시 예외 발생")
    void createFriendRequestToSelf() {
        // given
        String loginId = "user1";
        FriendRequestDto requestDto = new FriendRequestDto(requester.getId());

        when(memberService.findByLoginId(loginId)).thenReturn(requester);
        when(memberRepository.findById(requester.getId())).thenReturn(Optional.of(requester));

        // when & then
        assertThrows(SelfFriendRequestException.class, () ->
                friendRequestService.create(loginId, requestDto)
        );

        verify(friendRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("친구 요청이 없는 경우 빈 목록 반환")
    void findAllWithEmptyRequests() {
        // given
        String loginId = "user1";

        when(memberService.findByLoginId(loginId)).thenReturn(requester);
        when(friendRequestRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        FrListResponse response = friendRequestService.findAll(loginId);

        // then
        assertEquals(requester.getNickName(), response.requesterName());
        assertTrue(response.list().isEmpty());
    }

    @Test
    @DisplayName("친구 요청 목록 조회 성공")
    void findAllFriendRequests() {
        // given
        String loginId = "user1";

        Member receiver2 = new Member("user3", "123", "nick3", null, LocalDate.of(2000, 1, 1), "url" ,"ㅎㅇㅎㅇ");
        ReflectionTestUtils.setField(receiver, "id", 3L);

        FriendRequest request1 = new FriendRequest(requester, receiver, Status.PENDING);
        FriendRequest request2 = new FriendRequest(requester, receiver2, Status.PENDING);

        List<FriendRequest> requests = Arrays.asList(request1, request2);

        when(memberService.findByLoginId(loginId)).thenReturn(requester);
        when(friendRequestRepository.findAll()).thenReturn(requests);

        // when
        FrListResponse response = friendRequestService.findAll(loginId);

        // then
        assertEquals(requester.getNickName(), response.requesterName());
        assertEquals(2, response.list().size());

        // 첫 번째 응답 검증
        FrResponse frResponse1 = response.list().get(0);
        assertEquals(receiver.getId(), frResponse1.receiverId());
        assertEquals(receiver.getNickName(), frResponse1.receiverName());
        assertEquals(receiver.getImageUrl(), frResponse1.receiverImageUrl());
    }
}
