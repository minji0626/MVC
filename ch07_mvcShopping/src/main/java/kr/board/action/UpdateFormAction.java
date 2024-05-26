package kr.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.StringUtil;

public class UpdateFormAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) {// 로그인 미실시
			return "redirect:/member/loginForm.do";
		}
		
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		BoardDAO dao = BoardDAO.getInstance();
		BoardVO board = dao.getBoard(board_num);
		if(user_num!=board.getMem_num()) {
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		// 큰 따옴표 처리해주기
		// 수정폼의 input 태그에서 큰 따옴표 오류 보정
		board.setTitle(StringUtil.parseQuot(board.getTitle()));
		
		// 로그인이 되어있고 로그인한 회원 번호와 작성자 회원 번호가 일치할 때
		 request.setAttribute("board", board);
		 
		return "/WEB-INF/views/board/updateForm.jsp";
	}

}
