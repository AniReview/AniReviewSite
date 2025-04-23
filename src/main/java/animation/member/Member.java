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
// 멤버 프로필 업데이트를 어떻게 처리할건지??
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

    private int friendCount = 0;

    private String introduce;

    private boolean isDeleted = false;

    protected Member() {
    }

    public Member(String loginId,
                  String password,
                  String nickName,
                  Character character,
                  LocalDate birth,
                  String imageUrl,
                  String introduce) {
        this.loginId = loginId;
        this.password = password;
        this.nickName = nickName;
        this.character = character;
        this.birth = birth;
        this.imageUrl = imageUrl;
        this.introduce = introduce;
    }

    public void findByPassword(String password) {
        if (!this.getPassword().equals(SecurityUtils.sha256EncryptHex2(password))) {
            throw new NoSuchElementException("비밀번호가 다릅니다.");
        }
    }

    public void deleteMember() {
        isDeleted = true;
    }

    public void UpdateMyChar(Character character) {
        this.character = character;
    }

    public void updateProfile(String nickName, LocalDate birth,String introduce) {
        if (nickName != null) {
            this.nickName = nickName;
        }
        if (birth != null) {
            this.birth = birth;
        }
        if (introduce != null) {
            this.introduce = introduce;
        }
    }




}
