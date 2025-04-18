package animation.aniCharacter;

import animation.DatabaseCleanup;
import animation.admin.Admin;
import animation.anime.dto.AnimeCreateResponse;
import animation.character.Character;
import animation.loginUtils.JwtProvider;
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

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AniCharacterAPITest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @MockitoBean
    private AmazonS3 amazonS3;

    @Autowired
    JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        RestAssured.port = port;
    }

    /*
    //api 호출 안될 수도 있음
    @Test
    void 애니_캐릭터_관계_저장() throws InterruptedException {
        //애니 저장
        AnimeCreateResponse animeCreateResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post("/animes/{id}", 4286L)
                .then()
                .statusCode(200)
                .extract()
                .as(AnimeCreateResponse.class);

        Thread.sleep(1000);
        //캐릭터 저장
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post("/characters/{id}", 183656L)
                .then()
                .statusCode(200);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post("/characters/{id}", 75410L)
                .then()
                .statusCode(200);

        Thread.sleep(1000);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post("/characters/{id}", 166828L)
                .then()
                .statusCode(200);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post("/characters/{id}", 149860L)
                .then()
                .statusCode(200);

        Thread.sleep(1000);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post("/characters/{id}", 138657L)
                .then()
                .statusCode(200);

        Thread.sleep(1000);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post("/characters/{id}", 138655L)
                .then()
                .statusCode(200);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post("/characters/{id}", 138656L)
                .then()
                .statusCode(200);

        Thread.sleep(1000);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post("/animes/{animeMalId}/characters", animeCreateResponse.malId())
                .then()
                .statusCode(200);
    }*/
}
