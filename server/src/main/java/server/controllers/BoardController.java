package server.controllers;


import commons.Board;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.services.BoardService;

@RestController
@RequestMapping("/boards")
public class BoardController {

	private final BoardService boardService;

	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	@GetMapping("/get/{joinKey}")
	public Board getBoard(@PathVariable String joinKey) {
		return boardService.getBoardWithKey(joinKey);
	}
}
