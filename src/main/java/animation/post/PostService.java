package animation.post;

import animation.post.dto.PostCreateResponse;
import animation.post.dto.PostSaveRequest;
import animation.post.imageUrl.ImageRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    final private PostRepository postRepository;
    final private ImageRepository imageRepository;

    public PostService(PostRepository postRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
    }
//아직 안됨
    public PostCreateResponse create(PostSaveRequest postSaveRequest) {
       return null;
    }
}
