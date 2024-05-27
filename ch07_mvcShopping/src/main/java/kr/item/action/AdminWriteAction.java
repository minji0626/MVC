package kr.item.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.item.dao.ItemDAO;
import kr.item.vo.ItemVO;
import kr.util.FileUtil;

public class AdminWriteAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Integer user_num = 
				(Integer)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/member/loginForm.do";
		}
		
		Integer user_auth = 
				(Integer)session.getAttribute("user_auth");
		if(user_auth != 9) {//관리자로 로그인하지 않은 경우
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		//관리자로 로그인한 경우
		//전송된 데이터 인코딩 타입 지정
		request.setCharacterEncoding("utf-8");
		ItemVO item = new ItemVO();
		item.setName(request.getParameter("name"));
		item.setPrice(Integer.parseInt(
				         request.getParameter("price")));
		item.setQuantity(Integer.parseInt(
				         request.getParameter("quantity")));
		item.setPhoto1(FileUtil.createFile(request, "photo1"));
		item.setPhoto2(FileUtil.createFile(request, "photo2"));
		item.setDetail(request.getParameter("detail"));
		item.setStatus(Integer.parseInt(
				            request.getParameter("status")));
		
		ItemDAO dao = ItemDAO.getInstance();
		dao.insertItem(item);
		
		//Refresh 정보를 응답 헤더에 추가
		String url = request.getContextPath() + "/item/adminList.do";
		response.addHeader("Refresh", "2;url="+url);
		request.setAttribute("result_title", "상품 등록 완료");
		request.setAttribute("result_msg", "성공적으로 등록되었습니다.");
		request.setAttribute("result_url", url);
		
		return "/WEB-INF/views/common/result_view.jsp";
	}

}





