package animation.friendRequest;

import animation.DatabaseCleanup;
import animation.S3.S3Service;
import animation.friendRequest.dto.FriendRequestDto;
import animation.friendRequest.dto.FriendRequestResponseDto;
import animation.member.dto.MemberCreateRequest;
import animation.member.dto.MemberLoginRequest;
import animation.member.dto.MemberResponse;
import com.amazonaws.services.s3.AmazonS3;
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

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class friendRequestRestAssuredTest {

    @LocalServerPort
    int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @MockitoBean
    private AmazonS3 amazonS3;

    @MockitoBean
    private S3Service s3Service;

    @BeforeEach
    void setUp() throws IOException {
        databaseCleanup.execute();
        RestAssured.port = port;
    }

    @Test
    void 친구요청Test() throws Exception {

        File imageFile = new File("src/test/test-image.jpg");

        MemberCreateRequest memberCreateRequest1 = new MemberCreateRequest(
                "user1","testpassword","username1",null,
                LocalDate.of(1995, 8, 15),"ㅎㅇㅎㅇ"
        );

        String memberCreateJson1 = objectMapper.writeValueAsString(memberCreateRequest1);

        MemberResponse memberResponse1 = given()
                .when().log().all()
                .contentType("multipart/form-data")
                .multiPart("images", imageFile, "image/jpeg")
                .multiPart("memberCreateRequest", memberCreateJson1, "application/json")
                .post("/members")
                .then()
                .extract()
                .as(MemberResponse.class);

        MemberCreateRequest memberCreateRequest2 = new MemberCreateRequest(
                "user2","testpassword","username2",null,
                LocalDate.of(1990, 8, 15),"하이"
        );

        String memberCreateJson2 = objectMapper.writeValueAsString(memberCreateRequest2);

        MemberResponse memberResponse2 = given()
                .when().log().all()
                .contentType("multipart/form-data")
                .multiPart("images", imageFile, "image/jpeg")
                .multiPart("memberCreateRequest", memberCreateJson2, "application/json")
                .post("/members")
                .then()
                .extract()
                .as(MemberResponse.class);

        String token = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new MemberLoginRequest("user1", "testpassword"))
                .when()
                .post("/members/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");

        // 친구 요청
        FriendRequestResponseDto friendRequest = given()
                .when().log().all()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(new FriendRequestDto(memberResponse2.id()))
                .post("/friend-request")
                .then()
                .statusCode(200)
                .extract()
                .as(FriendRequestResponseDto.class);

        assertThat(friendRequest.requestMemberId()).isEqualTo(memberResponse1.id());
        assertThat(friendRequest.receiverMemberId()).isEqualTo(memberResponse2.id());
    }

    @Test
    void 나에게친구요청오류Test() throws Exception {

        File imageFile = new File("src/test/test-image.jpg");

        MemberCreateRequest memberCreateRequest1 = new MemberCreateRequest(
                "user1","testpassword","username1",null,
                LocalDate.of(1995, 8, 15),"ㅎㅇㅎㅇ"
        );

        String memberCreateJson1 = objectMapper.writeValueAsString(memberCreateRequest1);

        MemberResponse memberResponse1 = given()
                .when().log().all()
                .contentType("multipart/form-data")
                .multiPart("images", imageFile, "image/jpeg")
                .multiPart("memberCreateRequest", memberCreateJson1, "application/json")
                .post("/members")
                .then()
                .extract()
                .as(MemberResponse.class);

        String token = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new MemberLoginRequest("user1", "testpassword"))
                .when()
                .post("/members/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");

        given()
                .when().log().all()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(new FriendRequestDto(memberResponse1.id()))
                .post("/friend-request")
                .then()
                .statusCode(400);
    }
}
