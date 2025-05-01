package animation.friendRequest;

import animation.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    @Query("SELECT fr.requester FROM FriendRequest fr WHERE fr.receiver = :receiver")
    List<Member> findRequesterByReceiver(Member receiver);
}
