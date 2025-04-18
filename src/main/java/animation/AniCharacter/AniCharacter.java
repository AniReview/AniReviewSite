package animation.AniCharacter;

import animation.anime.Anime;
import animation.character.Character;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class AniCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Character character;

    @ManyToOne
    private Anime anime;

    protected AniCharacter() {
    }

    public AniCharacter(Character character, Anime anime) {
        this.character = character;
        this.anime = anime;
    }
}
