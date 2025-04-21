package animation.loveAni;

import animation.loginUtils.LoginMember;
import animation.loveAni.dto.DeleteLoveAniResponse;
import animation.loveAni.dto.LoveRequest;
import animation.loveAni.dto.LoveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoveAniController {

    private final LoveAniService loveAniService;

    @PostMapping("/loves")
    public LoveResponse create(@LoginMember String memberLoginId, @RequestBody LoveRequest loveRequest){
        return loveAniService.create(memberLoginId, loveRequest);
    }

    @DeleteMapping("/loves/{loveId}")
    public DeleteLoveAniResponse delete(@LoginMember String memberLoginId,
                                        @PathVariable Long loveId){
        return loveAniService.delete(memberLoginId, loveId);
    }

}
