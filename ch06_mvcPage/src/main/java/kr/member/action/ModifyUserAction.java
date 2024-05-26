package kr.member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.member.dao.MemberDAO;
import kr.member.vo.MemberVO;

public class ModifyUserAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) {// 로그인 미실시
			return "redirect:/member/loginForm.do";
		}
		// 로그인 완료
		request.setCharacterEncoding("UTF-8");
		
		// 자바빈 생성
		MemberVO member = new MemberVO();
		member.setMem_num(user_num);
		member.setName(request.getParameter("name"));
		member.setAddress1(request.getParameter("address1"));
		member.setAddress2(request.getParameter("address2"));
		member.setPhone(request.getParameter("phone"));
		member.setEmail(request.getParameter("email"));
		member.setZipcode(request.getParameter("zipcode"));
		
		MemberDAO dao = MemberDAO.getInstance();
		dao.updateMember(member);
		
		request.setAttribute("notice_msg", "회원 정보 수정 완료");
		request.setAttribute("notice_url", request.getContextPath()+"/member/myPage.do");
		return "/WEB-INF/views/common/alert_view.jsp";
	}

}
