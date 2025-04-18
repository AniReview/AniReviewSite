package animation.board;

import animation.board.dto.*;
import jakarta.persistence.GeneratedValue;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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

    public List<BoardResponse> read() {
        return boardRepository.findAll().stream()
                .map(board -> new BoardResponse(
                        board.getId(),
                        board.getBoardTitle()
                ))
                .toList();
    }
    @Transactional
    public BoardUpdateResponse update(Long boardId, BoardSaveRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("id를 찾을 수 없습니다."));
        board.update(request.boardTitle());
        return new BoardUpdateResponse(board.getId(),board.getBoardTitle(),board.getUpdatedAt());
    }

    @Transactional
    public BoardDeleteResponse delete(Long boardId) {
       boardRepository.deleteById(boardId);
        return new BoardDeleteResponse("게시판이 성공적으로 삭제되었습니다.",boardId);
    }
}
