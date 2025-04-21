package animation.bookmark;

import animation.bookmark.dto.*;
import animation.loginUtils.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @PostMapping("/bookmarks")
    public BookMarkResponse create(@LoginMember String memberLoginId,
                                   @RequestBody BookmarkRequest bookmarkRequest){
        return bookMarkService.create(memberLoginId, bookmarkRequest);
    }

    @GetMapping("/bookmarks/{memberId}")
    public BookmarkReadResponse read(@PathVariable Long memberId){
        return bookMarkService.readAll(memberId);
    }

    @DeleteMapping("/bookmarks/{bookmarkId}")
    public BookMarkDeleteResponse delete(@PathVariable Long bookmarkId,
                                         @LoginMember String memberToken){
        return bookMarkService.delete(bookmarkId, memberToken);
    }
}
