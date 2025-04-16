package animation.character;

import animation.DatabaseCleanup;
import animation.admin.Admin;
import animation.admin.AdminRepository;
import animation.character.dto.*;
import animation.loginUtils.JwtProvider;
import com.amazonaws.services.s3.AmazonS3;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CharacterAPITest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @MockitoBean
    private AmazonS3 amazonS3;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    AdminRepository adminRepository;

    Character character;
    Admin admin;

    String[] korInitials = {"가", "나", "다", "라", "마", "바", "사", "아", "자", "차", "카", "타", "파", "하"};
    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        RestAssured.port = port;

        // 테스트 데이터 추가 (캐릭터 15개 생성)
        for(int i=1; i<=15; i++) {
            String initial = korInitials[(i - 1) % korInitials.length];
            Character c = new Character(
                    initial + "캐릭터" + String.format("%02d", i),
                    "image" + i + ".jpg",
                    "소개" + i
            );
            // i번 만큼 increaseFavoriteCount 호출
            for (int j = 0; j < i; j++) {
                c.increaseFavoriteCount();
            }
            characterRepository.save(c);

            if (i == 1) {
                character = c;
            }
        }
        admin = new Admin("testId","password","testNickName","testImage");
        adminRepository.save(admin);
    }

    @Test
    @DisplayName("캐릭터 조회 : 최애캐순으로 정렬")
    public void 캐릭터_조회1() {
        RestAssured
                .given().log().all()
                .when()
                .get("/characters")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(CharacterPageResponse.class);
    }

    @Test
    @DisplayName("캐릭터 조회 : 가나다 순으로 정렬")
    public void 캐릭터_조회2() {
        RestAssured
                .given().log().all()
                .queryParam("orderBy", OrderBy.ALPHABETICAL)
                .when()
                .get("/characters")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(CharacterPageResponse.class);
    }

    @Test
    void 캐릭터_상세조회() {
        CharacterDetailResponse response = RestAssured
                .given().log().all()
                .pathParam("characterId", character.getId())
                .when()
                .get("/characters/{characterId}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(CharacterDetailResponse.class);

        assertThat(response.characterId()).isEqualTo(character.getId());
        assertThat(response.charName()).isEqualTo(character.getName());
        assertThat(response.imageUrl()).isEqualTo(character.getImageUrl());
        assertThat(response.charAbout()).isEqualTo(character.getAbout());
    }

    @Test
    void 캐릭터_수정() {
        // given
        CharacterUpdateRequest updateRequest = new CharacterUpdateRequest(
                "수정된이름", "updated.jpg", "수정된 소개"
        );

        String adminToken = jwtProvider.createToken(admin.getLoginId());

        // when
        CharacterUpdateResponse response = RestAssured
                .given().log().all()
                .pathParam("characterId", character.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/characters/{characterId}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(CharacterUpdateResponse.class);

        // then
        assertThat(response.characterId()).isEqualTo(character.getId());
        assertThat(response.charName()).isEqualTo("수정된이름");
        assertThat(response.charImageUrl()).isEqualTo("updated.jpg");
        assertThat(response.charAbout()).isEqualTo("수정된 소개");
    }

    @Test
    void 캐릭터_삭제_성공() {
        String adminToken = jwtProvider.createToken(admin.getLoginId());
        // when
        CharacterDeleteResponse response = RestAssured
                .given().log().all()
                .pathParam("characterId", character.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .when()
                .delete("/characters/{characterId}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(CharacterDeleteResponse.class);

        // then
        assertThat(response.characterId()).isEqualTo(character.getId());

        // 소프트 삭제 확인
        Character deletedCharacter = characterRepository.findById(character.getId()).get();
        assertThat(deletedCharacter.isDeleted()).isTrue();
    }

    @Test
    void 존재하지_않는_관리자_토큰_실패() {
        // given: 유효하지 않은 토큰
        String invalidToken = jwtProvider.createToken("invalidLoginId");

        // when & then
        RestAssured
                .given().log().all()
                .pathParam("characterId", character.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)
                .when()
                .delete("/characters/{characterId}")
                .then().log().all()
                .statusCode(404); // 관리자 없음 예외
    }

    @Test
    void 존재하지_않는_캐릭터_삭제_실패() {
        // given: 존재하지 않는 캐릭터 ID
        Long invalidCharacterId = 999L;
        String adminToken = jwtProvider.createToken(admin.getLoginId());

        // when & then
        RestAssured
                .given().log().all()
                .pathParam("characterId", invalidCharacterId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .when()
                .delete("/characters/{characterId}")
                .then().log().all()
                .statusCode(404); // 캐릭터 없음 예외
    }


    @Test
    @DisplayName("캐릭터 조회 : 삭제된 데이터는 보이지 않는다")
    void 캐릭터_조회_삭제된_데이터_제외() {
        // given: 테스트 데이터 중 하나를 soft delete 처리
        character.delete();
        characterRepository.save(character);

        // when: 전체 캐릭터 조회
        CharacterPageResponse response = RestAssured
                .given().log().all()
                .when()
                .get("/characters")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(CharacterPageResponse.class);


        List<Long> longs = response.characterResponseList().stream()
                .map(characterResponse -> characterResponse.characterId())
                .toList();

        assertThat(longs).doesNotContain(character.getId());
    }



}

