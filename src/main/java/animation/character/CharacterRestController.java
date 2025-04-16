package animation.character;

import animation.character.dto.*;
import animation.loginUtils.LoginMember;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class CharacterRestController {

    private final CharacterService characterService;

    public CharacterRestController(CharacterService characterService) {
        this.characterService = characterService;
    }

    //외부 api 를 받아와서 저장하는 로직
    @PostMapping("/characters/{id}")
    public JikanCharacterResponse save(@PathVariable Long id){
        return characterService.saveCharacterById(id);
    }

    @GetMapping("/characters")
    public CharacterPageResponse create(@RequestParam(required = false, defaultValue = "POPULAR") OrderBy orderBy,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return characterService.findAll(orderBy, pageable);
    }

    @GetMapping("/characters/{characterId}")
    public CharacterDetailResponse findById(@PathVariable Long characterId){
        return characterService.findById(characterId);
    }

    @PutMapping("/characters/{characterId}")
    public CharacterUpdateResponse update(@LoginMember String adminLoginId,
                                          @PathVariable Long characterId,
                                          @RequestBody CharacterUpdateRequest request){
        return characterService.update(adminLoginId,characterId, request);
    }

    @DeleteMapping("/characters/{characterId}")
    public CharacterDeleteResponse delete(@LoginMember String adminLoginId,
                                          @PathVariable Long characterId){
        return characterService.delete(adminLoginId, characterId);

    }


}
