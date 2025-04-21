package animation.loveAni;

import animation.anime.Anime;
import animation.character.Character;
import animation.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity
public class LoveAni {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Anime anime;

    protected LoveAni() {
    }

    public LoveAni(Member member, Anime anime) {
        this.member = member;
        this.anime = anime;
    }
}
