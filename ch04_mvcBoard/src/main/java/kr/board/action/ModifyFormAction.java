package kr.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;

public class ModifyFormAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		int num = Integer.parseInt(request.getParameter("num"));
		BoardVO boardVO = new BoardVO();
		BoardDAO dao = BoardDAO.getInstance();
		boardVO = dao.getBoard(num);
		
		request.setAttribute("boardVO", boardVO);
		
		return "/WEB-INF/views/modifyForm.jsp";
	}

}
