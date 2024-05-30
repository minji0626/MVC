package kr.cart.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;

import kr.cart.dao.CartDAO;
import kr.cart.vo.CartVO;
import kr.controller.Action;
import kr.item.dao.ItemDAO;
import kr.item.vo.ItemVO;

public class ModifyCartAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,String> mapAjax = 
				          new HashMap<String,String>();
		
		HttpSession session = request.getSession();
		Integer user_num = 
				(Integer)session.getAttribute("user_num");
		if(user_num==null) {
			mapAjax.put("result", "logout");
		}else {
			//전송된 데이터 인코딩 타입 지정
			request.setCharacterEncoding("utf-8");
			//전송된 데이터 반환
			int item_num = Integer.parseInt(
					     request.getParameter("item_num"));
			int order_quantity = Integer.parseInt(
					     request.getParameter("order_quantity"));
			
			ItemDAO itemDAO = ItemDAO.getInstance();
			ItemVO item = itemDAO.getItem(item_num);
			if(item.getStatus()==1) {//상품 미표시
				mapAjax.put("result", "noSale");
			}else if(item.getQuantity() < order_quantity) {
				//상품 재고 수량보다 장바구니에 담은 구매 수량이 더 많음
				mapAjax.put("result", "overQuantity");
			}else {
				//표시 상품이며 재고가 부족하지 않음
				CartVO cart = new CartVO();
				cart.setCart_num(Integer.parseInt(
						request.getParameter("cart_num")));
				cart.setOrder_quantity(order_quantity);
				
				CartDAO cartDAO = CartDAO.getInstance();
				//구매 수량 변경
				cartDAO.updateCart(cart);
				
				mapAjax.put("result", "success");
			}			
		}
		//JSON 문자열로 변환
		ObjectMapper mapper = new ObjectMapper();
		String ajaxData = mapper.writeValueAsString(mapAjax);
		
		request.setAttribute("ajaxData", ajaxData);
		
		return "/WEB-INF/views/common/ajax_view.jsp";
	}
	
}





