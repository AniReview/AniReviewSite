package animation.bookmark;

import animation.anime.Anime;
import animation.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Anime anime;

    @ManyToOne
    private Member member;

    protected BookMark() {
    }

    public BookMark(Anime anime, Member member) {
        this.anime = anime;
        this.member = member;
    }
}
