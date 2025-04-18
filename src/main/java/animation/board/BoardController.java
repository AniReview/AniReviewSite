package animation.board;

import animation.board.dto.*;
import animation.loginUtils.LoginMember;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoardController {
    final private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/boards")
    public BoardCreateResponse create(@RequestBody BoardSaveRequest boardSaveRequest, @LoginMember String adminId){
        return boardService.create(boardSaveRequest);
    }
    @GetMapping("/boards")
    public List<BoardResponse> read(){
        return boardService.read();
    }
    @PatchMapping("/boards/{boardId}")
    public BoardUpdateResponse update(@LoginMember String adminId,
                                      @PathVariable Long boardId,
                                      @RequestBody BoardSaveRequest request){
       return boardService.update(boardId,request);
    }
    @DeleteMapping("boards/{boardId}")
    public BoardDeleteResponse delete(@LoginMember String adminId,
                                      @PathVariable Long boardId){
        return boardService.delete(boardId);
    }

}
