package kr.member.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.member.dao.MemberDAO;
import kr.member.vo.MemberVO;

public class MyPageAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) { // 로그인 되지 않은 경우
			return "redirect:/member/loginForm.do";
		}
		
		// 로그인 완료 후
		MemberDAO dao = MemberDAO.getInstance();
		MemberVO member = dao.getMember(user_num);
		
		// 관심 게시물 정보
		BoardDAO boardDAO = BoardDAO.getInstance();
		List<BoardVO> boardList = boardDAO.getListBoardFav(1, 5, user_num);
		
		request.setAttribute("member", member);
		request.setAttribute("boardList", boardList);
		
		return "/WEB-INF/views/member/myPage.jsp";
	}

}
