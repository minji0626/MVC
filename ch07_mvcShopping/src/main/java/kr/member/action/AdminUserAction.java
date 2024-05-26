package kr.member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.member.dao.MemberDAO;

public class AdminUserAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) {// 로그인 미실시
			return "redirect:/member/loginForm.do";
		}
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		
		if(user_auth != 9) {
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		//  관리자로 로그인한 경우
		// 전송된 데이터 인코딩 타입 지정
		request.setCharacterEncoding("utf-8");
		
		// 전송된 데이터 변환
		int mem_num = Integer.parseInt(request.getParameter("mem_num"));
		int auth = Integer.parseInt(request.getParameter("auth"));
		
		MemberDAO dao = MemberDAO.getInstance();
		dao.updateMemberByAdmin(auth, mem_num);
		
		request.setAttribute("mem_num", mem_num);
		request.setAttribute("notice_msg", "회원 등급 수정이 완료되었습니다.");
		request.setAttribute("notice_url", request.getContextPath()+"/member/adminUserForm.do?mem_num="+mem_num);
		return "/WEB-INF/views/common/alert_view.jsp"; 	 
	}

}
