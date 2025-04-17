package animation.post;

import animation.loginUtils.LoginMember;
import animation.post.dto.PostCreateResponse;
import animation.post.dto.PostSaveRequest;
import animation.post.imageUrl.ImageRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {
    final private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //아직 service 구현 안됨
    @PostMapping("/posts")
    public PostCreateResponse create(@RequestBody PostSaveRequest postSaveRequest,
                                     @LoginMember String memberId){
        return postService.create(postSaveRequest);
    }
}
