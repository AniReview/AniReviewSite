package animation.admin;

import animation.admin.dto.AdminCreate;
import animation.admin.dto.AdminResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    @Test
    void 회원가입() {

        AdminResponse response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new AdminCreate("testId","password","testNickName","testImage"))
                .when()
                .post("/admins")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(AdminResponse.class);
    }
}
