package animation.character;

import animation.character.dto.JikanCharacterResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
