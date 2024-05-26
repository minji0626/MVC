package kr.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;

public class DetailAction  implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 전송된 데이터 인코딩 타입 지정
        request.setCharacterEncoding("utf-8");
        int num = Integer.parseInt(request.getParameter("num"));
        BoardVO boardVO = new BoardVO();
        BoardDAO dao = BoardDAO.getInstance();
        boardVO = dao.getBoard(num);

        // 데이터를 request 객체에 저장하여 JSP로 전달
        request.setAttribute("boardVO", boardVO);

		return "/WEB-INF/views/detail.jsp";
	}

}
