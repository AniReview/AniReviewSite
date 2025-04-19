package animation.member;

import animation.character.Character;
import animation.loginUtils.SecurityUtils;
import animation.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Getter
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String password;

    private String nickName;

    @ManyToOne
    private Character character;

    private LocalDate birth;

    private String imageUrl;

    private boolean isDeleted;

    protected Member() {
    }

    public Member(String loginId,
                  String password,
                  String nickName,
                  Character character,
                  LocalDate birth,
                  String imageUrl) {
        this.loginId = loginId;
        this.password = password;
        this.nickName = nickName;
        this.character = character;
        this.birth = birth;
        this.imageUrl = imageUrl;
    }

    public void findByPassword(String password) {
        if (!this.getPassword().equals(SecurityUtils.sha256EncryptHex2(password))) {
            throw new NoSuchElementException("비밀번호가 다릅니다.");
        }
    }


}
