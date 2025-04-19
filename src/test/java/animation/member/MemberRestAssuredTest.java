package animation.member;

import animation.DatabaseCleanup;
import animation.S3.S3Service;
import animation.anime.AnimeRepository;
import animation.anime.AnimeService;
import animation.character.Character;
import animation.character.CharacterRepository;
import animation.loginUtils.JwtProvider;
import animation.member.dto.MemberCreateRequest;
import animation.member.dto.MemberCreateResponse;
import animation.member.dto.MemberLoginRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberRestAssuredTest {

    @LocalServerPort
    int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @MockitoBean
    private AmazonS3 amazonS3;

    @Autowired
    CharacterRepository characterRepository;

    @MockitoBean
    private S3Service s3Service;

    @BeforeEach
    void setUp() throws IOException {
        databaseCleanup.execute();
        when(s3Service.uploadFile(any(MultipartFile.class)))
                .thenReturn("https://test-bucket.s3.amazonaws.com/test-image.jpg");
        RestAssured.port = port;
    }

    @Test
    void 최애캐있는멤버생성Test() throws Exception {

        File imageFile = new File("src/test/test-image.jpg");

        Character character = new Character(
                "에렌 예거",
                "image1.jpg",
                "진격의 거인 계승자",
                1L);

        characterRepository.save(character);

        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "testId","testpassword","testName",character.getId(),
                LocalDate.of(1995, 8, 15)
        );

        String memberCreateJson = objectMapper.writeValueAsString(memberCreateRequest);

        MemberCreateResponse memberCreateResponse = given()
                .when().log().all()
                .contentType("multipart/form-data")
                .multiPart("images", imageFile, "image/jpeg")
                .multiPart("memberCreateRequest", memberCreateJson, "application/json")
                .post("/members")
                .then()
                .extract()
                .as(MemberCreateResponse.class);

        assertThat(memberCreateResponse.id()).isEqualTo(1L);
        assertThat(memberCreateResponse.myChar()).isNotNull();
    }

    @Test
    void 최애캐없는멤버생성Test() throws Exception {
        File imageFile = new File("src/test/test-image.jpg");

        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "testId","testpassword","testName",null,
                LocalDate.of(1995, 8, 15)
        );

        String memberCreateJson = objectMapper.writeValueAsString(memberCreateRequest);

        MemberCreateResponse memberCreateResponse = given()
                .when().log().all()
                .contentType("multipart/form-data")
                .multiPart("images", imageFile, "image/jpeg")
                .multiPart("memberCreateRequest", memberCreateJson, "application/json")
                .post("/members")
                .then()
                .extract()
                .as(MemberCreateResponse.class);

        assertThat(memberCreateResponse.id()).isEqualTo(1L);
        assertThat(memberCreateResponse.myChar()).isNull();
    }

    @Test
    void 로그인Test() throws Exception {
        File imageFile = new File("src/test/test-image.jpg");

        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "testId","testpassword","testName",null,
                LocalDate.of(1995, 8, 15)
        );

        String memberCreateJson = objectMapper.writeValueAsString(memberCreateRequest);

        MemberCreateResponse memberCreateResponse = given()
                .when().log().all()
                .contentType("multipart/form-data")
                .multiPart("images", imageFile, "image/jpeg")
                .multiPart("memberCreateRequest", memberCreateJson, "application/json")
                .post("/members")
                .then()
                .extract()
                .as(MemberCreateResponse.class);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new MemberLoginRequest("testId","testpassword"))
                .when()
                .post("/members/login")
                .then()
                .statusCode(200);

    }

    @Test
    void 로그인비밀번호불일치Test() throws Exception{
        File imageFile = new File("src/test/test-image.jpg");

        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "testId","testpassword","testName",null,
                LocalDate.of(1995, 8, 15)
        );

        String memberCreateJson = objectMapper.writeValueAsString(memberCreateRequest);

        MemberCreateResponse memberCreateResponse = given()
                .when().log().all()
                .contentType("multipart/form-data")
                .multiPart("images", imageFile, "image/jpeg")
                .multiPart("memberCreateRequest", memberCreateJson, "application/json")
                .post("/members")
                .then()
                .extract()
                .as(MemberCreateResponse.class);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new MemberLoginRequest("testId","failedPassword"))
                .when()
                .post("/members/login")
                .then()
                .statusCode(404);
    }
}
