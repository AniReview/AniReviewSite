package animation.post.imageUrl;

import animation.post.Post;
import animation.post.PostRepository;
import animation.post.imageUrl.dto.imgCreateRequest;
import animation.post.imageUrl.dto.imgCreateResponse;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ImageService {
    final private ImageRepository imageRepository;
    final private PostRepository postRepository;

    public ImageService(ImageRepository imageRepository, PostRepository postRepository) {
        this.imageRepository = imageRepository;
        this.postRepository = postRepository;
    }

    public imgCreateResponse create(imgCreateRequest request) {
        Post post = postRepository.findById(request.postId()).orElseThrow(() -> new NoSuchElementException("id가 없습니다."));
        ImageUrl img = imageRepository.save(new ImageUrl(post,request.imgUrl()));
        return new imgCreateResponse(img.getId(),img.getPost().getId(),img.getImgUrl());
    }
}
