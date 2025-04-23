package animation.bookmark;

import animation.bookmark.dto.*;
import animation.loginUtils.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public BookMarkPageResponse read(@PathVariable Long memberId,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return bookMarkService.readAll(memberId, pageable);
    }

    @DeleteMapping("/bookmarks/{bookmarkId}")
    public BookMarkDeleteResponse delete(@PathVariable Long bookmarkId,
                                         @LoginMember String memberToken){
        return bookMarkService.delete(bookmarkId, memberToken);
    }
}
