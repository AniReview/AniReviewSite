package animation.admin;

import animation.DatabaseCleanup;
import animation.S3.S3Service;
import animation.admin.dto.AdminCreate;
import animation.admin.dto.AdminResponse;
import com.amazonaws.services.s3.AmazonS3;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;


import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminTest {

    @LocalServerPort
    int port;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private S3Service s3Service;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUp() throws IOException {
        RestAssured.port = port;
        when(s3Service.uploadFile(any(MultipartFile.class)))
                .thenReturn("https://test-bucket.s3.amazonaws.com/test-image.jpg");
        databaseCleanup.execute();
    }

    @Test
    void 프로필_이미지_업로드_및_회원가입_성공() throws Exception {
        // 1. 테스트용 이미지 파일 준비
        File imageFile = new File("src/test/test-image.jpg");

        // 2. AdminCreate 객체를 JSON 문자열로 변환
        AdminCreate adminCreate = new AdminCreate("testid", "testpassword", "testnickname");
        String adminCreateJson = objectMapper.writeValueAsString(adminCreate);

        // 3. multipart/form-data 요청 전송 및 응답 검증
        AdminResponse response = given().log().all()
                .contentType("multipart/form-data")
                .multiPart("images", imageFile, "image/jpeg")
                .multiPart("adminCreate", adminCreateJson, "application/json")
                .when()
                .post("/profileupload")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(AdminResponse.class);

        assertThat(response.imageUrl()).isEqualTo("https://test-bucket.s3.amazonaws.com/test-image.jpg");
    }
}
