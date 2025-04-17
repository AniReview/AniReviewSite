package animation.board;

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

}
