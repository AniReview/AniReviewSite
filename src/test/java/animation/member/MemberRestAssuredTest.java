package animation.member;

import animation.DatabaseCleanup;
import animation.anime.AnimeRepository;
import animation.anime.AnimeService;
import animation.character.Character;
import animation.character.CharacterRepository;
import animation.loginUtils.JwtProvider;
import animation.member.dto.MemberCreateRequest;
import com.amazonaws.services.s3.AmazonS3;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberRestAssuredTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @MockitoBean
    private AmazonS3 amazonS3;

    @Autowired
    CharacterRepository characterRepository;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        RestAssured.port = port;
    }

    @Test
    void 최애캐있는멤버생성Test() {

        Character character = new Character(
                "에렌 예거",
                "image1.jpg",
                "진격의 거인 계승자",
                1L);

        characterRepository.save(character);

        given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(new MemberCreateRequest(
                        "testId",
                        "testPassword",
                        "testNickName",
                        character.getId(),
                        LocalDate.of(1995, 8, 15),
                        "https://example.com/images/profile.jpg"
                        ))
                .post("/members")
                .then()
                .statusCode(200);
    }

    @Test
    void 최애캐없는멤버생성Test() {

        given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(new MemberCreateRequest(
                        "testId",
                        "testPassword",
                        "testNickName",
                        null,
                        LocalDate.of(1995, 8, 15),
                        "https://example.com/images/profile.jpg"
                ))
                .post("/members")
                .then()
                .statusCode(200);
    }
}
