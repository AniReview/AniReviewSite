package animation.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudFrontService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Value("${cloud.aws.s3.domain}")
    private String cloudFrontDomainName;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID().toString();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());
        metadata.setHeader("Content-Disposition", "inline");

        try (InputStream inputStream = multipartFile.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    fileName,
                    inputStream,
                    metadata
            );

            amazonS3.putObject(putObjectRequest);
        }

        return cloudFrontDomainName + "/" +fileName;
    }
}
