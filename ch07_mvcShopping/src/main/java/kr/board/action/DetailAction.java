package kr.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.StringUtil;

public class DetailAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		
		BoardVO board = new BoardVO();
		BoardDAO dao = BoardDAO.getInstance();
		dao.updateReadcount(board_num);
		
		board = dao.getBoard(board_num);
		board.setTitle(StringUtil.useNoHTML(board.getTitle()));
		board.setContent(StringUtil.useBrNoHTML(board.getContent()));
		request.setAttribute("board", board);
		
		return "/WEB-INF/views/board/detail.jsp";
	}

}
