package animation.anime;

import animation.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Anime extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;

    private String imageUrl;

    private String type;

    private String supervision;

    private List<String> genres = new ArrayList<>();

    private int episodes;

    private String rating;

    private LocalDateTime aired;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    private String studios;

    private String duration;

    private boolean airing;

    private int bookmark = 0;

    private boolean isDeleted = false;

    private Long malId;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void delete() {
        isDeleted = true;
    }

    protected Anime() {
    }

    public void increaseBookmark()  {
        bookmark++;
    }

    public void decreaseBookmark() {
        bookmark--;
    }

    public boolean parseUpComing() {
        return true;
    }

    public Anime(String title, String imageUrl, String type, String supervision, List<String> genres, int episodes, String rating, LocalDateTime aired, String synopsis, String productionCompany, String duration, boolean airing, Long malId) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.type = type;
        this.supervision = supervision;
        this.genres = genres;
        this.episodes = episodes;
        this.rating = rating;
        this.aired = aired;
        this.synopsis = synopsis;
        this.studios = productionCompany;
        this.duration = duration;
        this.airing = airing;
        this.malId = malId;
    }
}
