package kr.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import kr.order.vo.OrderDetailVO;
import kr.order.vo.OrderVO;
import kr.util.DBUtil;

public class OrderDAO {
	//싱글턴 패턴
	private static OrderDAO instance = new OrderDAO();
	
	public static OrderDAO getInstance() {
		return instance;
	}
	private OrderDAO() {}
	
	//주문 등록
	public void insertOrder(OrderVO order, 
			    List<OrderDetailVO> orderDetailList)
	                                     throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		PreparedStatement pstmt5 = null;
		ResultSet rs = null;
		String sql = null;
		int order_num = 0;
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//오토 커밋 해제
			conn.setAutoCommit(false);
			
			//order_num 구하기
			sql = "SELECT zorder_seq.nextval FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				order_num = rs.getInt(1);
			}
			
			//주문정보 처리
			sql = "INSERT INTO zorder (order_num,order_total,"
				+ "payment,receive_name,receive_post,receive_address1,"
				+ "receive_address2,receive_phone,notice,mem_num) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setInt(1, order_num);
			pstmt2.setInt(2, order.getOrder_total());
			pstmt2.setInt(3, order.getPayment());
			pstmt2.setString(4, order.getReceive_name());
			pstmt2.setString(5, order.getReceive_post());
			pstmt2.setString(6, order.getReceive_address1());
			pstmt2.setString(7, order.getReceive_address2());
			pstmt2.setString(8, order.getReceive_phone());
			pstmt2.setString(9, order.getNotice());
			pstmt2.setInt(10, order.getMem_num());
			pstmt2.executeUpdate();
			
			//주문상세정보 처리
			sql = "INSERT INTO zorder_detail (detail_num,item_num,"
				+ "item_name,item_price,item_total,order_quantity,"
				+ "order_num) VALUES (zorder_detail_seq.nextval,"
				+ "?,?,?,?,?,?)";
			pstmt3 = conn.prepareStatement(sql);
			
			for(int i=0;i<orderDetailList.size();i++) {
				OrderDetailVO orderDetail = orderDetailList.get(i);
				pstmt3.setInt(1, orderDetail.getItem_num());
				pstmt3.setString(2, orderDetail.getItem_name());
				pstmt3.setInt(3, orderDetail.getItem_price());
				pstmt3.setInt(4, orderDetail.getItem_total());
				pstmt3.setInt(5, orderDetail.getOrder_quantity());
				pstmt3.setInt(6, order_num);
				pstmt3.addBatch();//쿼리를 메모리에 올림
				
				//계속 추가하면 outOfMemory 발생, 1000개 단위로 executeBatch()
				if(i % 1000 == 0) {
					pstmt3.executeBatch();
				}
			}
			pstmt3.executeBatch();//쿼리를 전송
			
			//상품의 재고수 차감
			sql = "UPDATE zitem SET quantity=quantity-? WHERE item_num=?";
			pstmt4 = conn.prepareStatement(sql);
			for(int i=0;i<orderDetailList.size();i++) {
				OrderDetailVO orderDetail = orderDetailList.get(i);
				pstmt4.setInt(1, orderDetail.getOrder_quantity());
				pstmt4.setInt(2, orderDetail.getItem_num());
				pstmt4.addBatch();//쿼리를 메모리에 올림
				
				if(i % 1000 == 0) {
					pstmt4.executeBatch();
				}				
			}
			pstmt4.executeBatch();//쿼리 전송
			
			//장바구니에서 주문상품 삭제
			sql = "DELETE FROM zcart WHERE mem_num=?";
			pstmt5 = conn.prepareStatement(sql);
			pstmt5.setInt(1, order.getMem_num());
			pstmt5.executeUpdate();
			
			//모든 SQL문이 정상 수행
			conn.commit();
		}catch(Exception e) {
			//SQL문이 하나라도 실패
			conn.rollback();
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt5, null);
			DBUtil.executeClose(null, pstmt4, null);
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(rs, pstmt, conn);
		}
	}
	//관리자 - 전체 주문 개수/검색 주문 개수
	//관리자 - 전체 주문 목록/검색 주문 목록
	//사용자 - 전체 주문 개수/검색 주문 개수
	//사용자 - 전체 주문 목록/검색 주문 목록
	//개별 상품 목록
	//주문 삭제(삭제시 재고를 원상 복귀시키 않음, 주문 취소일 때 재고 수량 원상 복귀)
	//관리자/사용자 - 주문 상세
	//관리자/사용자 - 배송지정보 수정
	//관리자 - 배송상태 수정
	//사용자 - 주문 취소
	
}





