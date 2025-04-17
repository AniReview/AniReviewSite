package animation.character;

import animation.DatabaseCleanup;
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
public class CharDataImportTest {

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
    void saveCharacter_success() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post("/characters/{id}", 1L)
                .then()
                .statusCode(200);
    }

    @Test
    void saveCharacter_emptyData() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post("/characters/{id}", -1L)
                .then()
                .statusCode(500);
    }

}
