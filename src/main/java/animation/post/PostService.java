package animation.post;

import org.springframework.stereotype.Service;

@Service
public class PostService {
    final  private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
}
