package kr.order.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.cart.dao.CartDAO;
import kr.cart.vo.CartVO;
import kr.controller.Action;
import kr.item.dao.ItemDAO;
import kr.item.vo.ItemVO;
import kr.order.dao.OrderDAO;
import kr.order.vo.OrderDetailVO;
import kr.order.vo.OrderVO;

public class UserOrderAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Integer user_num = 
				(Integer)session.getAttribute("user_num");
		if(user_num==null) {//로그인이 되지 않은 경우
			return "redirect:/member/loginForm.do";
		}
		//로그인 된 경우
		//POST방식의 접근만 허용
		if(request.getMethod().toUpperCase().equals("GET")) {
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		//전송된 데이터 인코딩 타입 지정
		request.setCharacterEncoding("utf-8");
		
		CartDAO dao = CartDAO.getInstance();
		int all_total = dao.getTotalByMem_num(user_num);
		if(all_total<=0) {
			request.setAttribute("notice_msg", 
					 "정상적인 주문이 아니거나 상품의 수량이 부족합니다.");
			request.setAttribute("notice_url", 
				request.getContextPath()+"/item/itemList.do");
			return "/WEB-INF/views/common/alert_view.jsp";
		}
		
		//장바구니에 담겨있는 상품 정보 호출
		List<CartVO> cartList = dao.getListCart(user_num);
		
		//개별상품 정보 담기
		List<OrderDetailVO> orderDetailList = 
				           new ArrayList<OrderDetailVO>();
		ItemDAO itemDAO = ItemDAO.getInstance();
		for(CartVO cart : cartList) {
			ItemVO item = itemDAO.getItem(cart.getItem_num());
			if(item.getStatus() == 1) {
				//상품 미표시
				request.setAttribute("notice_msg", 
						"["+item.getName()+"]상품 판매 중지");
				request.setAttribute("notice_url", 
				   request.getContextPath()+"/cart/list.do");
				return "/WEB-INF/views/common/alert_view.jsp";
			}
			if(item.getQuantity() < cart.getOrder_quantity()) {
				//상품 재고 수량 부족
				request.setAttribute("notice_msg", 
					"["+item.getName()+"]재고수량 부족으로 주문 불가");
				request.setAttribute("notice_url", 
						   request.getContextPath()+"/cart/list.do");
				return "/WEB-INF/views/common/alert_view.jsp";
			}
			
			//자바빈에 개별상품 정보 저장
			OrderDetailVO orderDetail = new OrderDetailVO();
			orderDetail.setItem_num(cart.getItem_num());
			orderDetail.setItem_name(cart.getItemVO().getName());
			orderDetail.setItem_price(cart.getItemVO().getPrice());
			orderDetail.setOrder_quantity(cart.getOrder_quantity());
			orderDetail.setItem_total(cart.getSub_total());
			
			orderDetailList.add(orderDetail);
		}
		
		//구매정보 담기
		OrderVO order = new OrderVO();
		order.setOrder_total(all_total);
		order.setPayment(Integer.parseInt(
				         request.getParameter("payment")));
		order.setReceive_name(
				request.getParameter("receive_name"));
		order.setReceive_post(
				request.getParameter("receive_post"));
		order.setReceive_address1(
				request.getParameter("receive_address1"));
		order.setReceive_address2(
				request.getParameter("receive_address2"));
		order.setReceive_phone(
				request.getParameter("receive_phone"));
		order.setNotice(request.getParameter("notice"));
		order.setMem_num(user_num);
		
		OrderDAO orderDAO = OrderDAO.getInstance();
		orderDAO.insertOrder(order, orderDetailList);
		
		//Refresh 정보를 응답 헤더에 추가
		String url = request.getContextPath()+"/main/main.do";
		response.addHeader("Refresh", "2;url="+url);
		request.setAttribute("result_title", "상품 주문 완료");
		request.setAttribute("result_msg", "주문이 완료되었습니다.");
		request.setAttribute("result_url", url);		
		
		return "/WEB-INF/views/common/result_view.jsp";
	}

}





