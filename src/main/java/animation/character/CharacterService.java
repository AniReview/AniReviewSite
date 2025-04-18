package animation.character;

import animation.EmptyDataException;
import animation.admin.Admin;
import animation.admin.AdminRepository;
import animation.character.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CharacterService {

    private final WebClient webClient;
    private final CharacterRepository characterRepository;
    private final CharacterQueryRepository characterQueryRepository;
    private final AdminRepository adminRepository;

    public CharacterService(WebClient.Builder builder, CharacterRepository repository, CharacterQueryRepository characterQueryRepository, AdminRepository adminRepository) {
        this.webClient = builder.baseUrl("https://api.jikan.moe/v4").build();
        this.characterRepository = repository;
        this.characterQueryRepository = characterQueryRepository;
        this.adminRepository = adminRepository;
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
                        data.about(),
                        data.mal_id()
                )
        );
        return response;
    }

    public CharacterPageResponse findAll(OrderBy orderBy, Pageable pageable) {
        List<CharacterResponse> all = characterQueryRepository.findAll(orderBy, pageable);

        long totalCount = characterQueryRepository.countFiltered(orderBy, pageable);
        int totalPage = (int) Math.ceil((double) (totalCount - 1) / pageable.getPageSize()) + 1;
        return new CharacterPageResponse(
                totalPage,
                totalCount,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                all
        );
    }

    public CharacterDetailResponse findById(Long characterId) {
        Character character = characterRepository.findByIdAndIsDeletedFalse(characterId)
                .orElseThrow(() -> new NoSuchElementException("캐릭터를 찾을 수 없습니다."));
        return new CharacterDetailResponse(
                characterId,
                character.getName(),
                character.getImageUrl(),
                character.getAbout()
        );
    }

    @Transactional
    public CharacterUpdateResponse update(String adminLoginId, Long characterId, CharacterUpdateRequest request) {
        Admin admin = adminRepository.findByLoginId(adminLoginId)
                .orElseThrow(() -> new NoSuchElementException("관리자를 찾을 수 없습니다."));

        Character character = characterRepository.findByIdAndIsDeletedFalse(characterId)
                .orElseThrow(() -> new NoSuchElementException("캐릭터를 찾을 수 없습니다."));

        character.update(
                request.charName(),
                request.charImageUrl(),
                request.charAbout()
        );

        return new CharacterUpdateResponse(
                characterId,
                character.getName(),
                character.getImageUrl(),
                character.getAbout()
        );
    }

    @Transactional
    public CharacterDeleteResponse delete(String adminLoginId, Long characterId) {
        adminRepository.findByLoginId(adminLoginId)
                .orElseThrow(()-> new NoSuchElementException("관리자를 찾을 수 없습니다."));

        Character character = characterRepository.findByIdAndIsDeletedFalse(characterId)
                .orElseThrow(() -> new NoSuchElementException("이미 삭제된 캐릭터 입니다."));

        character.delete();

        return new CharacterDeleteResponse(
                characterId
        );
    }
}