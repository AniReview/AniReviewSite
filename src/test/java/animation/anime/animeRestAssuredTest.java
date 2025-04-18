package animation.anime;

import animation.DatabaseCleanup;
import animation.anime.dto.AnimeDetailResponse;
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

import java.time.LocalDateTime;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class animeRestAssuredTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @MockitoBean
    private AmazonS3 amazonS3;

    // 실제 서비스와 레포지토리를 사용
    @Autowired
    private AnimeService animeService;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    JwtProvider jwtProvider;

    // 테스트용 애니메이션 객체
    Anime anime;

    // 애니메이션 제목 배열
    String[] animeTitles = {"귀멸의 칼날", "진격의 거인", "원피스", "나루토", "블리치",
            "도쿄 리벤저스", "주술회전", "하이큐", "슬램덩크", "원펀맨",
            "코드 기아스", "스파이 패밀리", "강철의 연금술사", "전생슬라임", "헌터x헌터"};

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        RestAssured.port = port;

        // 테스트 데이터 추가 (애니메이션 15개 생성)
        for (int i = 1; i <= 15; i++) {
            Anime animes = new Anime(
                    animeTitles[i - 1],                               // 1. 제목
                    "https://example.com/anime" + i + ".jpg",       // 2. 이미지 URL
                    "TV",                                           // 3. 타입
                    "감독" + i,                                     // 4. 감독 (supervision - 빠져있던 항목)
                    Arrays.asList("액션", "판타지", (i % 2 == 0) ? "모험" : "코미디", (i % 3 == 0) ? "로맨스" : "SF"), // 5. 장르
                    i + 10,                                         // 6. 에피소드 수
                    "PG-13",                                        // 7. 등급
                    LocalDateTime.of(2010, 12, 28, 0, 0),  // 8. 방영일
                    "애니메이션 " + i + "의 줄거리입니다.",           // 9. 줄거리
                    "스튜디오 " + i,                                // 10. 제작사
                    "23분",                                         // 11. 재생시간
                    i % 5 == 0,                                     // 12. 방영 중 여부
                    (long) i                                        // 13. MAL ID
            );

            // 인기도 설정 (i번 만큼 인기도 증가)
            for (int j = 0; j < i; j++) {
                animes.increaseBookmark();
            }

            // 저장
            animeRepository.save(animes);

            // 첫 번째 애니메이션은 테스트용으로 따로 저장
            if (i == 1) {
                anime = animes;
            }
        }
    }

    @Test
    void 전체인기순테스트() {
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

    @Test
    void 애니상세조회테스트() {
        AnimeDetailResponse detailResponse = given()
                .when()
                .pathParam("animeId", anime.getId())
                .get("/animes/{animeId}")
                .then()
                .statusCode(200)
                .extract()
                .as(AnimeDetailResponse.class);

        assertThat(detailResponse.title()).isEqualTo("귀멸의 칼날");
    }
}
