package animation.post.imageUrl;

import animation.loginUtils.LoginMember;
import animation.post.imageUrl.dto.imgCreateRequest;
import animation.post.imageUrl.dto.imgCreateResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    final private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }
    @PostMapping("/imgs")
    public imgCreateResponse create(@RequestBody imgCreateRequest request,
                                    @LoginMember String memberId){
        return imageService.create(request);
    }
}
