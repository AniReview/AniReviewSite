package animation.character;

import animation.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Character extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String about;

    private int favoriteCount = 0;

    private boolean isDeleted = false;

    private Long malId;

    protected Character() {
    }

    public Character(String name, String imageUrl, String about, Long malId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.about = about;
        this.malId = malId;
    }

    public void update(String name, String imageUrl, String about) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.about = about;
    }

    public void increaseFavoriteCount() {
        this.favoriteCount++;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
