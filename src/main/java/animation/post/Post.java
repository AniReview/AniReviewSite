package animation.post;

import animation.board.Board;
import animation.post.imageUrl.ImageUrl;
import jakarta.persistence.*;
import lombok.Getter;

import java.lang.reflect.Member;
import java.util.List;

@Getter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private int commentCount;

    @ManyToOne
    private Board board;

    @OneToMany
    private List<ImageUrl> imageUrlList;

    @ManyToOne
    private Member member;

    private boolean hasImage;

    public Post(String title, String content, Board board, List<ImageUrl> imageUrlList, Member member, boolean hasImage) {
        this.title = title;
        this.content = content;
        this.board = board;
        this.imageUrlList = imageUrlList;
        this.member = member;
        this.hasImage = hasImage;
        this.commentCount = commentCount;
    }

    protected Post() {
    }
    public void addCommentCount(){
      this.commentCount++;
    }
}
