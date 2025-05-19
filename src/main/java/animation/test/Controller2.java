package animation.test;

import animation.S3.CloudFrontService;
import animation.S3.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
public class Controller2 {

    private final Repository2 repository2;
    private final CloudFrontService cloudFrontService;

    @PostMapping("/test/cloud-front-image")
    public String cloudFrontUpload(@RequestPart(value = "images") MultipartFile files) throws IOException {
        String url = cloudFrontService.uploadFile(files);
        TestEntity2 testEntity = new TestEntity2(url);
        repository2.save(testEntity);
        return testEntity.getImageUrl();
    }

    @GetMapping("/test/cloud-front-image")
    public List<String> findAll() {
        List<TestEntity2> entityList = repository2.findAll();
        return entityList.stream().map(t -> t.getImageUrl()).toList();
    }
}
