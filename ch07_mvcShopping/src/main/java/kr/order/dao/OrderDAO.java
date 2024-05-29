package kr.order.dao;

public class OrderDAO {
	private static OrderDAO instance = new OrderDAO();

	public static OrderDAO getInstance() {
		return instance;
	}

	private OrderDAO() {
	}
	
	// 주문 등록
	// 관리자 - 전체 주문 개수 / 검색 주문 개수
	// 관리자 - 전체 주문 목록 / 검색 주문 목록
	// 사용자 - 전제 주문 개수 /  검색 주문 개수
	// 사용자 - 전체 주문 목록 / 검색 주문 목록
	// 개별 상품 목록
	// 주문 삭제(삭제시 재고를 원상복구 시키지 않음, 주문 취소일 때 재고 수량 원상 복구)
	// 관리자/사용자 - 주문 상세
	// 관리자/사용자 - 배송지 정보 수정하기
	// 관리자 - 배송 상태 수정
	// 사용자 - 주문 취소
	
	
}
