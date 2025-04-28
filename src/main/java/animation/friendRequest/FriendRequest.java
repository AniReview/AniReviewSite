package animation.friendRequest;

import animation.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

import static animation.friendRequest.Status.PENDING;

@Getter
@Entity
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // 요청 하는 사람
    private Member requester;

    @ManyToOne // 요청 받는 사람
    private Member receiver;

    @Enumerated(EnumType.STRING)
    private Status status;

    protected FriendRequest() {}

    public FriendRequest(Member requester, Member receiver, Status status) {
        this.requester = requester;
        this.receiver = receiver;
        this.status = status;
    }

    public void updateStatus(Status status) {
        if (status == PENDING) {
            throw new IllegalStateException("수락 또는 거절만 가능합니다");
        }
        this.status = status;
    }

}
