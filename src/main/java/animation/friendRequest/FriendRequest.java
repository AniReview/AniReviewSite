package animation.friendRequest;

import animation.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member requester;

    @ManyToOne
    private Member receiver;

    @Enumerated(EnumType.STRING)
    private Status status;

    protected FriendRequest() {}

    public FriendRequest(Member requester, Member receiver, Status status) {
        this.requester = requester;
        this.receiver = receiver;
        this.status = status;
    }

    public void myself(Member receiver) {
        if (Objects.equals(requester.getId(), receiver.getId())) {
            throw new RuntimeException("자기 자신입니다.");
        }
    }
}
