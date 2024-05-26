package kr.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;

public class DeleteAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		
		BoardVO boardVO = new BoardVO();
		boardVO.setNum(Integer.parseInt(request.getParameter("num")));
		boardVO.setPasswd(request.getParameter("passwd"));
		
		BoardDAO dao = BoardDAO.getInstance();
		BoardVO db_board = dao.getBoard(boardVO.getNum());
		
		boolean check = false;
		if(db_board != null) { // 게시물이 존재할 때 비밀번호 확인
			check = db_board.isCheckedPassword(boardVO.getPasswd());
		}
		if(check) { // 비밀번호가 일치할 때 delete 메소드 실행
			dao.delete(boardVO.getNum());
		} 
		request.setAttribute("check", check);
		
		return "/WEB-INF/views/delete.jsp";
	}

}
