package animation.test;

import animation.S3.S3Service;
import animation.loginUtils.LoginMember;
import animation.member.dto.MemberResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
public class Controller {

    private final S3Service s3Service;
    private final Repository repository;

    @PostMapping("/test/image")
    public String imageUpdate(@RequestPart(value = "images") MultipartFile files) throws IOException {
        String url = s3Service.uploadFile(files);
        TestEntity testEntity = new TestEntity(url);
        repository.save(testEntity);
        return testEntity.getImageUrl();
    }

    @GetMapping("/test/image")
    public List<String> findAll() {
        List<TestEntity> entityList = repository.findAll();
        return entityList.stream().map(t -> t.getImageUrl()).toList();
    }
}
