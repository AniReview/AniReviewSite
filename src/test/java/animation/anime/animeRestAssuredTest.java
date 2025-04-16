package animation.anime;

import animation.DatabaseCleanup;
import animation.character.OrderBy;
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
public class animeRestAssuredTest {

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


    @Test
    void saveAnime_success() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post("/animes/{id}", 1L)
                .then()
                .statusCode(200);
    }

    @Test
    void 애니전체조회테스트() {
        RestAssured
                .given()
                .when()
                .get("/animes")
                .then()
                .statusCode(200);
    }

    @Test
    void 애니방영중조회테스트() {
        RestAssured
                .given()
                .when()
                .queryParam("airing", AnimeFilter.AIRING)
                .get("/animes")
                .then()
                .statusCode(200);
    }

}
