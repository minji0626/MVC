package kr.order.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.cart.dao.CartDAO;
import kr.cart.vo.CartVO;
import kr.controller.Action;
import kr.item.dao.ItemDAO;

public class UserOrderFormAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) {
			return "redirect:/member/loginForm.do";
		}
		// 로그인이 된 경우
		//POST 방식의 접근만 허용한다
		if(request.getMethod().toUpperCase().equals("GET")) {
			return "/WEB-INF/views/common/notice.jsp";
		}
		
		CartDAO dao = CartDAO.getInstance();
		int all_total = dao.getTotalByMem_num(user_num); // 주문 금액을 장바구니에서 가져온다
		if(all_total <= 0) {
			request.setAttribute("notice_msg", "정상적인 주문이 아니거나 상품의 수량이 부족합니다.");
			request.setAttribute("notice_url", request.getContextPath()+"/item/itemList.do");
			return "/WEB-INF/views/common/alert_view.jsp";
		}
		
		// 장바구니에 담겨있는 상품 정보 호출
		List<CartVO> cartList = dao.getListCart(user_num);
		
		// 판매 중지가 된 상품이 있다면 구매할 수 없다고 알려주고, 해당 상품을 장바구니에서 삭제시켜야 함
		ItemDAO itemDAO = ItemDAO.getInstance();
		for(CartVO cart : cartList) {
			
		}
		
		return null;
		
	}
	

}
