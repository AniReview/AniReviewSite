package animation.anime;

import animation.DatabaseCleanup;
import animation.anime.dto.AnimeCreateResponse;
import animation.loginUtils.JwtProvider;
import com.amazonaws.services.s3.AmazonS3;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class animeRestAssuredTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @MockitoBean
    private AmazonS3 amazonS3;

    @MockitoBean
    private AnimeService animeService;

    @Autowired
    JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        RestAssured.port = port;
    }

    private AnimeCreateResponse DummyData(Long malId) {
        return new AnimeCreateResponse(
                1L,
                "귀멸의 칼날",
                "TV",
                "https://example.com/kimetsu.jpg",
                26,
                "PG-13",
                LocalDateTime.of(2019, 4, 6, 0, 0),
                "혈귀에 의해 가족을 잃고 여동생마저 혈귀로 변해버린 주인공 탄지로가 혈귀를 물리치고 여동생을 인간으로 되돌리기 위해 여행을 떠난다.",
                Arrays.asList("액션", "판타지", "모험", "역사"),
                "ufotable",
                "23분",
                false,
                malId
        );
    }



    @Test
    void testImportAnime() {
        // 테스트용 데이터 준비
        Long malId = 12345L;
        AnimeCreateResponse ani = DummyData(malId);

        // 서비스 모킹 설정
        Mockito.when(animeService.importAnimeById(malId)).thenReturn(ani);

        // API 호출 및 검증
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/animes/{malId}", malId)
                .then()
                .statusCode(200)
                .body("id", equalTo(ani.id().intValue()))
                .body("title", equalTo(ani.title()))
                .body("type", equalTo(ani.type()))
                .body("images", equalTo(ani.images()))
                .body("episodes", equalTo(ani.episodes()))
                .body("rating", equalTo(ani.rating()))
                .body("synopsis", equalTo(ani.synopsis()))
                .body("genres.size()", equalTo(ani.genres().size()))
                .body("genres", containsInAnyOrder(ani.genres().toArray()))
                .body("studios", equalTo(ani.studios()))
                .body("duration", equalTo(ani.duration()))
                .body("airing", equalTo(ani.airing()))
                .body("malId", equalTo(ani.malId().intValue()));

    }

    @Test
    void 애니전체조회테스트() {
        given()
                .when()
                .get("/animes")
                .then()
                .statusCode(200);
    }

    @Test
    void 애니방영중조회테스트() {
        given()
                .when()
                .queryParam("airing", AnimeFilter.AIRING)
                .get("/animes")
                .then()
                .statusCode(200);
    }

}
