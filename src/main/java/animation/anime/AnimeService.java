package animation.anime;

import animation.anime.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import animation.anime.dto.AnimeCreateResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AnimeService {

    private final WebClient webClient;
    private final AnimeRepository animeRepository;

    public AnimeService(WebClient webClient, AnimeRepository animeRepository) {
        this.webClient = webClient;
        this.animeRepository = animeRepository;
    }

    public AnimeCreateResponse importAnimeById(Long malId) {
        JikanApiResponse apiResponse = fetchAnimeFromApi(malId);
        AnimeCreateResponse response = convertToAnimeResponse(apiResponse);
        Anime anime = saveAnimeEntity(response);
        log.info("애니메이션 저장 완료: ID={}", anime.getId());
        return toAnimeResponse(anime);
    }

    private JikanApiResponse fetchAnimeFromApi(Long malId) {
        return webClient.get()
                .uri("/anime/{id}", malId)
                .retrieve()
                .bodyToMono(JikanApiResponse.class)
                .timeout(Duration.ofSeconds(30))
                .block();
    }

    private AnimeCreateResponse convertToAnimeResponse(JikanApiResponse apiResponse) {
        JikanData data = apiResponse.data();

        // 이미지 URL 추출
        String imageUrl = Optional.ofNullable(data.images())
                .map(JikanImages::jpg)
                .map(JikanJpg::image_url)
                .orElse("");

        // 장르 이름 리스트 추출
        List<String> genres = data.genres().stream()
                .map(JikanGenre::name)
                .collect(Collectors.toList());

        // 스튜디오 이름을 하나의 문자열로 변환
        String studios = data.studios().stream()
                .map(JikanStudio::name)
                .collect(Collectors.joining(", "));

        // 방영일 변환
        LocalDateTime airedDate = parseAiredDate(data.aired());


        return new AnimeCreateResponse(
                data.mal_id(),
                data.title(),
                data.type(),
                imageUrl,
                data.episodes(),
                data.rating(),
                airedDate,
                data.synopsis(),
                genres,
                studios,
                data.duration(),
                data.airing()
        );
    }

    private LocalDateTime parseAiredDate(JikanAired aired) {
        if (aired == null || aired.from() == null || aired.from().isEmpty()) {
            return null;
        }

        try {
            return LocalDateTime.parse(aired.from(), DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            log.warn("날짜 변환 실패: {}", aired.from());
            return null;
        }
    }

    private Anime saveAnimeEntity(AnimeCreateResponse response) {
        Anime anime = new Anime(
                response.title(),
                response.images(),
                response.type(),
                "Unknown", // 감독 정보는 API에 명확히 제공되지 않음
                response.genres(),
                response.episodes(),
                response.rating(),
                response.aired(),
                response.synopsis(),
                response.studios(),
                response.duration(),
                response.airing()
        );

        return animeRepository.save(anime);
    }
    public AnimeCreateResponse toAnimeResponse(Anime anime) {
        return new AnimeCreateResponse(
                anime.getId(),
                anime.getTitle(),
                anime.getType(),
                anime.getImageUrl(),
                anime.getEpisodes(),
                anime.getRating(),
                anime.getAired(),
                anime.getSynopsis(),
                anime.getGenres(),
                anime.getStudios(),
                anime.getDuration(),
                anime.isAiring()
        );
    }

    public AnimePageResponse findAll() {
        List<Anime> animeList = animeRepository.findAll();
        List<AnimeResponse> responseList = animeList.stream().map(
                a -> new AnimeResponse(
                        a.getId(),
                        a.getImageUrl(),
                        a.getTitle()))
                .toList();

        return new AnimePageResponse(responseList);
    }

}


