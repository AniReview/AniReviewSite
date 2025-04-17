package animation.character;

import animation.DatabaseCleanup;
import animation.loginUtils.JwtProvider;
import com.amazonaws.services.s3.AmazonS3;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;

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

    @Mock
    WebClient webClient;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        RestAssured.port = port;
    }

//    문제 상황: api 호출하는 테스트가 계속 되다가 지금 안됨
//    왜?: 왜냐면 api를 호출하는데에는 api 서버 상태가 안좋으면 안주고, 네트워크가 안좋으면 안되고, 여러가지 경우가 있다.
//    근데 왜 인텔리제이에서는 통과하고, CI에서는 불통이야?
//    일반적으로 우리 컴터는 인터넷에 자유롭게 연결되어 있고, Jikan API에 바로 요청이 가능 but, CI에서는 외부 인터넷 접근을 막아 두거나, 프록시를 통해서만 허용
//    -> 그래서 상황에 따라 지칸api에 호출하면 에러가 날 수도 있음
//    api 호출을 하는 테스트는 WireMock를 해야 된다.
//    WireMock이란?
//    WireMock은 HTTP 서버를 모킹(mocking)하는 라이브러리로, 실제 외부 API와의 통신을 대체하여 원하는 응답을 반환하게 할 수 있다.
    /*@Test
    void saveCharacter_success() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post("/characters/{id}", 1L)
                .then()
                .log().all()
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
                .log().all()
                .statusCode(500);
    }*/

}
