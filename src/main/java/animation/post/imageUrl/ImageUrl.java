package animation.post.imageUrl;

import animation.board.Board;
import animation.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
@Entity
public class ImageUrl extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Board board;

    private String imgUrl;

    public ImageUrl(Board board, String imgUrl) {
        this.board = board;
        this.imgUrl = imgUrl;
    }

    protected ImageUrl() {
    }
}
