package kr.order.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.order.dao.OrderDAO;
import kr.order.vo.OrderVO;

public class AdminModifyAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Integer user_num = 
				(Integer)session.getAttribute("user_num");
		if(user_num==null) {
			return "redirect:/member/loginForm.do";
		}
		
		Integer user_auth = 
				(Integer)session.getAttribute("user_auth");
		if(user_auth!=9) {//관리자로 로그인하지 않은 경우
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		//전송된 데이터 인코딩 타입 지정
		request.setCharacterEncoding("utf-8");
		
		int order_num = Integer.parseInt(
				  request.getParameter("order_num"));
		OrderDAO dao = OrderDAO.getInstance();
		//주문정보 호출
		OrderVO db_order = dao.getOrder(order_num);		
		//배송지 수정전 배송상태를 한번 더 체크함
		if(db_order.getStatus()>1) {
			//배송준비중 이상으로 관리자가 변경한 상품을 주문자가 변경할 수 없음
			request.setAttribute("notice_msg", 
					"배송상태가 변경되어 주문자가 주문정보 변경 불가");
			request.setAttribute("notice_url", 
			     request.getContextPath()+"/order/adminDetail.do?order_num="+order_num);
			return "/WEB-INF/views/common/alert_view.jsp";
		}
		
		OrderVO order = new OrderVO();
		order.setOrder_num(order_num);
		order.setReceive_name(request.getParameter("receive_name"));
		order.setReceive_post(request.getParameter("receive_post"));
		order.setReceive_address1(request.getParameter("receive_address1"));
		order.setReceive_address2(request.getParameter("receive_address2"));
		order.setReceive_phone(request.getParameter("receive_phone"));
		order.setNotice(request.getParameter("notice"));
		
		//배송지정보 수정
		dao.updateOrder(order);
		
		request.setAttribute("notice_msg", 
				               "정상적으로 수정되었습니다.");
		request.setAttribute("notice_url", 
				request.getContextPath()
				    +"/order/adminDetail.do?order_num="+order_num);
		
		return "/WEB-INF/views/common/alert_view.jsp";
	}

}



