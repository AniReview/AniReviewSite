package animation.post;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {
    final private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }
}
