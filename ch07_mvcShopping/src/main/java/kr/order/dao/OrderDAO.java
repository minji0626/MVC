package kr.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.order.vo.OrderDetailVO;
import kr.order.vo.OrderVO;
import kr.util.DBUtil;
import oracle.jdbc.babelfish.BabelfishPreparedStatement;

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
	public int getOrderCount(String keyfield,String keyword)
			throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			if(keyword != null && !"".equals(keyword)) {
				//검색글 개수
				if(keyfield.equals("1")) sub_sql += "WHERE order_num=?";
				else if(keyfield.equals("2")) sub_sql += "WHERE id LIKE '%' || ? || '%'";
				else if(keyfield.equals("3")) sub_sql += "WHERE item_name LIKE '%' || ? || '%'";
			}
			//SQL문 작성
			sql = "SELECT COUNT(*) FROM zorder JOIN (SELECT order_num,"
					+ "LISTAGG(item_name,',') WITHIN GROUP (ORDER BY item_name) "
					+ "item_name FROM zorder_detail GROUP BY order_num) "
					+ "USING (order_num) JOIN zmember USING(mem_num) " + sub_sql;
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			if(keyword !=null && !"".equals(keyword)) {
				pstmt.setString(1, keyword);
			}
			//SQL문 실행
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return count;
	}
	//관리자 - 전체 주문 목록/검색 주문 목록
	public List<OrderVO> getListOrder(
			            int start, int end,
			            String keyfield,String keyword)
			                            throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrderVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			if(keyword != null && !"".equals(keyword)) {
				//검색글 개수
				if(keyfield.equals("1")) sub_sql += "WHERE order_num=?";
				else if(keyfield.equals("2")) sub_sql += "WHERE id LIKE '%' || ? || '%'";
				else if(keyfield.equals("3")) sub_sql += "WHERE item_name LIKE '%' || ? || '%'";
			}
			//SQL문 작성
			sql = "SELECT * FROM (SELECT a.*,rownum rnum FROM ("
				+ "SELECT * FROM zorder JOIN (SELECT order_num,"
				+ "LISTAGG(item_name,',') WITHIN GROUP (ORDER BY item_name) "
				+ "item_name FROM zorder_detail GROUP BY order_num) "
				+ "USING (order_num) JOIN zmember USING(mem_num) " + sub_sql
				+ " ORDER BY order_num DESC)a) "
				+ "WHERE rnum>=? AND rnum <=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			if(keyword !=null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
			
			//SQL문 실행
			rs = pstmt.executeQuery();
			list = new ArrayList<OrderVO>();
			while(rs.next()) {
				OrderVO order = new OrderVO();
				order.setOrder_num(rs.getInt("order_num"));
				order.setItem_name(rs.getString("item_name"));
				order.setOrder_total(rs.getInt("order_total"));
				order.setStatus(rs.getInt("status"));
				order.setReg_date(rs.getDate("reg_date"));
				order.setId(rs.getString("id"));
				
				list.add(order);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return list;
	}
	//사용자 - 전체 주문 개수/검색 주문 개수
	public int getOrderCountByMem_num(
			String keyfield,String keyword,int mem_num)
					throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			if(keyword != null && !"".equals(keyword)) {
				//검색글 개수
				if(keyfield.equals("1")) sub_sql += "AND item_name LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "AND order_num=?";
			}
			//SQL문 작성
			sql = "SELECT COUNT(*) FROM zorder JOIN (SELECT order_num,"
					+ "LISTAGG(item_name,',') WITHIN GROUP (ORDER BY item_name) "
					+ "item_name FROM zorder_detail GROUP BY order_num) "
					+ "USING (order_num) WHERE mem_num=? " + sub_sql;
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setInt(1, mem_num);
			if(keyword !=null && !"".equals(keyword)) {
				pstmt.setString(2, keyword);
			}
			//SQL문 실행
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return count;
	}
	//사용자 - 전체 주문 목록/검색 주문 목록
	public List<OrderVO> getListOrderByMem_num(
			int start,int end,
			String keyfield,String keyword,int mem_num)
					throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrderVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			if(keyword != null && !"".equals(keyword)) {
				//검색글 목록
				if(keyfield.equals("1")) sub_sql += "AND item_name LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "AND order_num=?";
			}
			//SQL문 작성
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM ("
					+ "SELECT * FROM zorder JOIN (SELECT order_num,"
					+ "LISTAGG(item_name,',') WITHIN GROUP (ORDER BY item_name) "
					+ "item_name FROM zorder_detail GROUP BY order_num) "
					+ "USING (order_num) WHERE mem_num=? " + sub_sql
					+ " ORDER BY order_num DESC)a) "
					+ "WHERE rnum>=? AND rnum<=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setInt(++cnt, mem_num);
			if(keyword !=null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
			//SQL문 실행
			rs = pstmt.executeQuery();
			list = new ArrayList<OrderVO>();
			while(rs.next()) {
				OrderVO order = new OrderVO();
				order.setOrder_num(rs.getInt("order_num"));
				order.setOrder_total(rs.getInt("order_total"));
				order.setItem_name(rs.getString("item_name"));
				order.setStatus(rs.getInt("status"));
				order.setReg_date(rs.getDate("reg_date"));

				list.add(order);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}			
		return list;
	}
	//개별 상품 목록
	public List<OrderDetailVO> getListOrderDetail(
			int order_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrderDetailVO> list = null;
		String sql = null;
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT * FROM zorder_detail WHERE order_num=? "
					+ "ORDER BY item_num DESC";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setInt(1, order_num);
			//SQL문 실행
			rs = pstmt.executeQuery();
			list = new ArrayList<OrderDetailVO>();
			while(rs.next()) {
				OrderDetailVO detail = new OrderDetailVO();
				detail.setItem_num(rs.getInt("item_num"));
				detail.setItem_name(rs.getString("item_name"));
				detail.setItem_price(rs.getInt("item_price"));
				detail.setItem_total(rs.getInt("item_total"));
				detail.setOrder_quantity(rs.getInt("order_quantity"));
				detail.setOrder_num(rs.getInt("order_num"));

				list.add(detail);
			}

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return list;
	}

	//주문 삭제(삭제시 재고를 원상 복귀시키 않음, 주문 취소일 때 재고 수량 원상 복귀)
	//관리자/사용자 - 주문 상세
	public OrderVO getOrder(int order_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrderVO order = null;
		String sql = null;
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT * FROM zorder WHERE order_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setInt(1, order_num);
			//SQL문 실행
			rs = pstmt.executeQuery();
			if(rs.next()) {
				order = new OrderVO();
				order.setOrder_num(rs.getInt("order_num"));
				order.setOrder_total(rs.getInt("order_total"));
				order.setPayment(rs.getInt("payment"));
				order.setStatus(rs.getInt("status"));
				order.setReceive_name(rs.getString("receive_name"));
				order.setReceive_post(rs.getString("receive_post"));
				order.setReceive_address1(rs.getString("receive_address1"));
				order.setReceive_address2(rs.getString("receive_address2"));
				order.setReceive_phone(rs.getString("receive_phone"));
				order.setNotice(rs.getString("notice"));
				order.setReg_date(rs.getDate("reg_date"));
				order.setMem_num(rs.getInt("mem_num"));
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return order;
	}
	//관리자/사용자 - 배송지정보 수정
	public void updateOrder(OrderVO order)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "UPDATE zorder SET receive_name=?,receive_post=?,"
					+ "receive_address1=?,receive_address2=?,receive_phone=?,"
					+ "notice=?,modify_date=SYSDATE WHERE order_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, order.getReceive_name());
			pstmt.setString(2, order.getReceive_post());
			pstmt.setString(3, order.getReceive_address1());
			pstmt.setString(4, order.getReceive_address2());
			pstmt.setString(5, order.getReceive_phone());
			pstmt.setString(6, order.getNotice());
			pstmt.setInt(7, order.getOrder_num());
			//SQL문 실행
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//관리자 - 배송상태 수정
	public void updateOrderStatus(OrderVO order)
			                       throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String sql = null;
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//오토 커밋 해제
			conn.setAutoCommit(false);
			
			sql = "UPDATE zorder SET status=?,modify_date=SYSDATE "
				+ "WHERE order_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, order.getStatus());
			pstmt.setInt(2, order.getOrder_num());
			pstmt.executeUpdate();
			
			//주문 취소일 경우만 상품개수 조정
			if(order.getStatus() == 5) {
				//주문번호에 해당하는 상품정보 구하기
				List<OrderDetailVO> detailList = 
						getListOrderDetail(order.getOrder_num());
				sql = "UPDATE zitem SET quantity=quantity+? WHERE item_num=?";
				pstmt2 = conn.prepareStatement(sql);
				for(int i=0;i<detailList.size();i++) {
					OrderDetailVO detail = detailList.get(i);
					pstmt2.setInt(1, detail.getOrder_quantity());
					pstmt2.setInt(2, detail.getItem_num());
					pstmt2.addBatch();
					
					if(i%1000==0) {
						pstmt2.executeBatch();
					}
				}//end of for
				pstmt2.executeBatch();
			}//end of if
			
			//모든 SQL문 성공하면 commit
			conn.commit();
		}catch(Exception e) {
			//SQL문이 하나라도 오류가 발생하면 rollback
			conn.rollback();
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt2, conn);
		}
	}
	//사용자 - 주문 취소
	public void updateOrderCancel(int order_num)
			throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String sql = null;
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//오토 커밋 해제
			conn.setAutoCommit(false);
			//SQL문 작성
			sql = "UPDATE zorder SET status=5,modify_date=SYSDATE "
					+ "WHERE order_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, order_num);
			pstmt.executeUpdate();

			//주문번호에 해당하는 상품정보 구하기
			List<OrderDetailVO> detailList = 
					getListOrderDetail(order_num);
			//주문 취소로 주문상품의 재고수 환원
			sql = "UPDATE zitem SET quantity=quantity + ? WHERE item_num=?";
			pstmt2 = conn.prepareStatement(sql);
			for(int i=0;i<detailList.size();i++) {
				OrderDetailVO detail = detailList.get(i);
				pstmt2.setInt(1, detail.getOrder_quantity());
				pstmt2.setInt(2, detail.getItem_num());
				pstmt2.addBatch();

				if(i%1000==0) {
					pstmt2.executeBatch();
				}
			}
			pstmt2.executeBatch();

			//모든 SQL문이 성공하면 커밋
			conn.commit();
		}catch(Exception e) {
			//SQL문이 하나라도 실패하면 롤백
			conn.rollback();
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
}













