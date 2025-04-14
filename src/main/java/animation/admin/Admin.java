package animation.admin;

import animation.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String nickName;

    private String password;

    private String imageUrl;

    protected Admin() {
    }

    public Admin(String loginId, String nickName, String password, String imageUrl) {
        this.loginId = loginId;
        this.nickName = nickName;
        this.password = password;
        this.imageUrl = imageUrl;
    }


}
