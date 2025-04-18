package animation.anime;

import animation.anime.dto.AnimePageResponse;
import animation.anime.dto.AnimeResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnimeServiceTest {

    @Mock
    private AnimeQueryRepository animeQueryRepository;

    @InjectMocks
    private AnimeService animeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    @DisplayName("애니 전체 조회")
    void findAll() {
        // given
        Pageable pageable = PageRequest.of(0, 3);
        AnimeFilter filter = AnimeFilter.ALL;

        List<AnimeResponse> mockList = Arrays.asList(
                new AnimeResponse(3L, "https://img1.jpg", "Attack on Titan",3),
                new AnimeResponse(2L, "https://img2.jpg", "Demon Slayer",2),
                new AnimeResponse(1L, "https://img3.jpg", "Your Name",1)
        );

        when(animeQueryRepository.findAll(pageable, filter)).thenReturn(mockList);

        // when
        AnimePageResponse result = animeService.findAll(pageable, filter);

        // then
        assertThat(result.responseList().get(0).title()).isEqualTo("Attack on Titan");



        verify(animeQueryRepository).findAll(pageable, filter);
    }

    @Test
    @DisplayName("캐릭터상세조회테스트")
    void detail() {


    }



}
