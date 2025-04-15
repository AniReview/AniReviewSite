package animation.character;

import animation.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Character extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String about;

    private int favoriteCount = 0;

    protected Character() {
    }

    public Character(String name, String imageUrl, String about) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.about = about;
    }
}
