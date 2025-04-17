package animation.board;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    final private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardCreateResponse create(BoardSaveRequest boardSaveRequest) {
        Board board = new Board(boardSaveRequest.boardTitle());
        boardRepository.save(board);
        return new BoardCreateResponse(
                board.getId(),
                board.getBoardTitle(),
                board.getCreatedAt()
        );
    }


}
