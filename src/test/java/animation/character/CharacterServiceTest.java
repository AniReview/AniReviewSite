package animation.character;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import animation.admin.Admin;
import animation.admin.AdminRepository;
import animation.character.dto.CharacterPageResponse;
import animation.character.dto.CharacterResponse;
import animation.character.dto.CharacterUpdateRequest;
import animation.character.dto.CharacterUpdateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

class CharacterServiceTest {

    @Mock
    private CharacterQueryRepository characterQueryRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private WebClient.Builder builder;

    @Mock
    private WebClient.Builder baseUrlBuilder;

    @Mock
    private WebClient webClient;

    private CharacterService characterService;

    private Admin admin;
    private Character character;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // WebClient.Builder mock 설정
        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(webClient);


        characterService = new CharacterService( builder,characterRepository, characterQueryRepository, adminRepository); // 생성자 직접 사용

        admin = new Admin("testId", "password", "testNickName", "testImage");

        character = new Character("캐릭터", "image.jpg", "소개");
    }


    @Test
    @DisplayName("캐릭터 전체 조회")
    void findAll() {
        // given
        OrderBy orderBy = OrderBy.ALPHABETICAL;
        Pageable pageable = PageRequest.of(0, 3);

        List<CharacterResponse> mockList = Arrays.asList(
                new CharacterResponse(1L, "짱구" ,"test1.jpg", 0),
                new CharacterResponse(2L, "훈이", "test2.jpg",1),
                new CharacterResponse(3L, "맹구", "test3.jpg",2)
        );
        long totalCount = 7L;

        when(characterQueryRepository.findAll(orderBy, pageable)).thenReturn(mockList);
        when(characterQueryRepository.countFiltered(orderBy, pageable)).thenReturn(totalCount);

        // when
        CharacterPageResponse result = characterService.findAll(orderBy, pageable);

        // then
        assertThat(result.totalPage()).isEqualTo(3); // (7-1)/3 + 1 = 3
        assertThat(result.totalCount()).isEqualTo(7L);
        assertThat(result.currentPage()).isEqualTo(0);
        assertThat(result.pageSize()).isEqualTo(3);
        assertThat(result.characterResponseList()).hasSize(3);
        assertThat(result.characterResponseList().get(0).charName()).isEqualTo("짱구");

        verify(characterQueryRepository).findAll(orderBy, pageable);
        verify(characterQueryRepository).countFiltered(orderBy, pageable);
    }

    @Test
    void 캐릭터_수정_성공() {
        // given
        String adminLoginId ="admin";
        Long characterId = 100L;
        CharacterUpdateRequest request = new CharacterUpdateRequest("수정이름", "updated.jpg", "수정소개");

        when(adminRepository.findByLoginId(adminLoginId)).thenReturn(Optional.of(admin));
        when(characterRepository.findByIdAndIsDeletedFalse(characterId)).thenReturn(Optional.of(character));

        // when
        CharacterUpdateResponse response = characterService.update(adminLoginId, characterId, request);

        // then
        assertThat(response.characterId()).isEqualTo(characterId);
        assertThat(response.charName()).isEqualTo("수정이름");
        assertThat(response.charImageUrl()).isEqualTo("updated.jpg");
        assertThat(response.charAbout()).isEqualTo("수정소개");
    }

    @Test
    void 관리자_없으면_예외() {
        // given
        String adminLoginId ="admin";
        Long characterId = 100L;
        CharacterUpdateRequest request = new CharacterUpdateRequest("수정이름", "updated.jpg", "수정소개");

        when(adminRepository.findByLoginId(adminLoginId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> characterService.update(adminLoginId, characterId, request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("관리자를 찾을 수 없습니다.");
    }

    @Test
    void 캐릭터_없으면_예외() {
        // given
        String adminLoginId ="admin";
        Long characterId = 100L;
        CharacterUpdateRequest request = new CharacterUpdateRequest("수정이름", "updated.jpg", "수정소개");

        when(adminRepository.findByLoginId(adminLoginId)).thenReturn(Optional.of(admin));
        when(characterRepository.findByIdAndIsDeletedFalse(characterId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> characterService.update(adminLoginId, characterId, request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("캐릭터를 찾을 수 없습니다.");
    }

    @Test
    void findById_삭제된_캐릭터_예외() {
        // given
        Long characterId = 1L;
        given(characterRepository.findByIdAndIsDeletedFalse(characterId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> characterService.findById(characterId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("캐릭터를 찾을 수 없습니다.");
    }
}
