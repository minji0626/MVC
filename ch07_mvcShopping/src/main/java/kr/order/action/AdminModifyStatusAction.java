package kr.order.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.order.dao.OrderDAO;
import kr.order.vo.OrderDetailVO;
import kr.order.vo.OrderVO;

public class AdminModifyStatusAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Integer user_num = 
				(Integer)session.getAttribute("user_num");
		if(user_num==null) {//로그인이 되지 않은 경우
			return "redirect:/member/loginForm.do";
		}
		
		Integer user_auth = 
				(Integer)session.getAttribute("user_auth");
		if(user_auth != 9) {//관리자로 로그인하지 않은 경우
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		//관리자로 로그인한 경우
		request.setCharacterEncoding("utf-8");
		
		OrderVO order = new OrderVO();
		order.setStatus(Integer.parseInt(
				        request.getParameter("status")));
		order.setOrder_num(Integer.parseInt(
		                request.getParameter("order_num")));
		
		OrderDAO dao = OrderDAO.getInstance();
		//주문정보 반환
		OrderVO db_order = dao.getOrder(order.getOrder_num());
		
		//사용자가 배송상태를 5로 변경했을 경우
		if(db_order.getStatus()==5) {
			request.setAttribute("notice_msg", 
			"사용자가 배송상태를 주문 취소로 변경해서 관리자가 배송상태 수정 불가");
			request.setAttribute("notice_url", 
				request.getContextPath()
				         +"/order/adminDetail.do?order_num="
						 +order.getOrder_num());
			return "/WEB-INF/views/common/alert_view.jsp";
		}
		
		//관리자 배송상태 변경
		dao.updateOrderStatus(order);
		
		request.setAttribute("notice_msg","정상적으로 수정되었습니다.");
		request.setAttribute("notice_url", 
				request.getContextPath()
					         +"/order/adminDetail.do?order_num="
							 +order.getOrder_num());
		
		return "/WEB-INF/views/common/alert_view.jsp";
	}

}





