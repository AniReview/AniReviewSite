package animation.post.imageUrl;

import animation.post.Post;
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
    private Post post;

    private String imgUrl;

    public ImageUrl(Post post,String imgUrl) {
        this.post = post;
        this.imgUrl = imgUrl;
    }

    protected ImageUrl() {
    }
}
