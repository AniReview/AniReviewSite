package animation.character;

import animation.EmptyDataException;
import animation.character.dto.CharacterData;
import animation.character.dto.JikanCharacterResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CharacterService {

    private final WebClient webClient;
    private final CharacterRepository characterRepository;

    public CharacterService(WebClient.Builder builder, CharacterRepository repository) {
        this.webClient = builder.baseUrl("https://api.jikan.moe/v4").build();
        this.characterRepository = repository;
    }

    public JikanCharacterResponse saveCharacterById(Long id) {
        JikanCharacterResponse response = webClient.get()
                .uri("/characters/{id}", id)
                .retrieve()
                .bodyToMono(JikanCharacterResponse.class)
                .block();

        if (response == null || response.data() == null) {
            throw new EmptyDataException("데이터가 비어있습니다.");
        }


        CharacterData data = response.data();

        characterRepository.save(
                new Character(
                        data.name(),
                        data.images().jpg().imageUrl(),
                        data.about()
                )
        );
        return response;
    }

}