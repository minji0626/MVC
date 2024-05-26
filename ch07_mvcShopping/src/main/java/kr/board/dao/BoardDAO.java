package kr.board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.board.vo.BoardFavVO;
import kr.board.vo.BoardReplyVO;
import kr.board.vo.BoardVO;
import kr.util.DBUtil;
import kr.util.DurationFromNow;
import kr.util.StringUtil;

public class BoardDAO {
	// 싱글턴 패턴
	private static BoardDAO instance = new BoardDAO();
	
	public static BoardDAO getInstance() {
		return instance;
	}
	private BoardDAO() {}
	
	// 글 등록하기
	public void insertBoard(BoardVO board) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn=DBUtil.getConnection();
			sql="INSERT INTO zboard(board_num,title,content,filename,ip,mem_num) VALUES(zboard_seq.nextval,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getContent());
			pstmt.setString(3, board.getFilename());
			pstmt.setString(4, board.getIp());
			pstmt.setInt(5, board.getMem_num());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	// 총 글의 개수, 검색 개수
	public int getBoardCount(String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql="";
		int count = 0;
		try {
			conn=DBUtil.getConnection();
			
			if(keyword != null && !"".equals(keyword)) {
				// 검색 처리
				if(keyfield.equals("1")) {
					sub_sql += "WHERE title LIKE '%' || ? || '%'";
				}
				else if(keyfield.equals("2")) {
					sub_sql += "WHERE id LIKE '%' || ? || '%'";
				}
				else if(keyfield.equals("3")) {
					sub_sql += "WHERE content LIKE '%' || ? || '%'";
				}
			}
			
			sql="SELECT COUNT(*) FROM zboard JOIN zmember USING(mem_num) " + sub_sql;
			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(1, keyword);
			}
			rs=pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}
	// 글 목록
	public List<BoardVO> getList(int start, int end, String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;
		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				// 검색 처리
				if(keyfield.equals("1")) {
					sub_sql += "WHERE title LIKE '%' || ? || '%'";
				}
				else if(keyfield.equals("2")) {
					sub_sql += "WHERE id LIKE '%' || ? || '%'";
				}else if(keyfield.equals("3")) {
					sub_sql += "WHERE content LIKE '%' || ? || '%'";
				}
			}
			
			sql = "SELECT * FROM(SELECT a.*,rownum rnum FROM (SELECT * FROM zboard JOIN zmember USING(mem_num) "
					+ sub_sql + " ORDER BY board_num DESC)a) WHERE rnum >=? AND rnum<=?";
			pstmt = conn.prepareStatement(sql);

			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
			rs= pstmt.executeQuery();
			
			list = new ArrayList<BoardVO>();
			while (rs.next()) {
				BoardVO board = new BoardVO();
				board.setId(rs.getString("id"));
				board.setBoard_num(rs.getInt("board_num"));
				board.setTitle(StringUtil.useNoHTML(rs.getString("title")));
				board.setReg_date(rs.getDate("reg_date"));
				board.setHit(rs.getInt("hit"));
				
				list.add(board);
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		
		return list;
	}
	
	// 글 상세
	public BoardVO getBoard(int board_num) throws Exception{
		BoardVO board = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();
			// 주의! 회원 탈퇴하면 zmember_detail의 레코드를 지우기 때문에 조인시 데이터 누락 방지를 위해 OUTER JOIN 사용
			sql = "SELECT * FROM zboard JOIN zmember USING(mem_num) LEFT OUTER JOIN zmember_detail USING(mem_num) WHERE board_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			rs = pstmt.executeQuery();
			while (rs.next()) {
			  board = new BoardVO();
			  board.setId(rs.getString("id"));
			  board.setBoard_num(rs.getInt("board_num"));
			  board.setTitle(rs.getString("title"));
			  board.setContent(rs.getString("content"));
			  board.setReg_date(rs.getDate("reg_date"));
			  board.setModify_date(rs.getDate("modify_date"));
			  board.setHit(rs.getInt("hit")); 
			  board.setFilename(rs.getString("filename"));
			  board.setIp(rs.getString("ip")); 
			  board.setPhoto(rs.getString("photo"));
			  board.setMem_num(rs.getInt("mem_num"));
			  }
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return board;	
	}
	// 조회수 증가
	public void updateReadcount(int board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			// 커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			sql = "UPDATE zboard SET hit=hit+1 WHERE board_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	// 파일 삭제
	public void deleteFile(int board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			// 커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			// SQL문 작성
			sql = "UPDATE zboard SET filename='' WHERE board_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	// 글 수정
	public void updateBoard(BoardVO board) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;
		try {
			// 커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			if(board.getFilename()!=null && !"".equals(board.getFilename())) {
				sub_sql += ", filename=?";
			}
			// SQL문 작성
			sql = "UPDATE zboard SET title=?,content=?,modify_date=SYSDATE,ip=?"+sub_sql+" WHERE board_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++cnt, board.getTitle());
			pstmt.setString(++cnt, board.getContent());
			pstmt.setString(++cnt, board.getIp());
			if(board.getFilename()!=null && !"".equals(board.getFilename())) {
				pstmt.setString(++cnt, board.getFilename());
			}
			pstmt.setInt(++cnt, board.getBoard_num());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	// 글 삭제
	public void deleteBoard(int board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			// 오토 커밋 해제
			conn.setAutoCommit(false);
			
			// 좋아요 삭제
			sql="DELETE FROM zboard_fav WHERE board_num=?";
			pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			pstmt.executeUpdate();
			
			// 댓글 삭제
			sql="DELETE FROM zboard_reply WHERE board_num=?";
			pstmt2= conn.prepareStatement(sql);
			pstmt2.setInt(1, board_num);
			pstmt2.executeUpdate();
			
			// 부모글 삭제
			sql ="DELETE FROM zboard WHERE board_num=?";
			pstmt3 = conn.prepareStatement(sql);
			pstmt3.setInt(1, board_num);
			pstmt3.executeUpdate();
			
			// 예외 발생 없이 정상적으로 SQL문 실행
			conn.commit();
		} catch (Exception e) {
			// 예외 발생
			conn.rollback();
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	
//	좋아요 등록
	public void insertFav(BoardFavVO favVO) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			// 커넥션 풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			// SQL문 작성
			sql = "INSERT INTO zboard_fav(board_num,mem_num) VALUES(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, favVO.getBoard_num());
			pstmt.setInt(2, favVO.getMem_num());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
//	좋아요 개수
	public int selectFavCount(int board_num) throws Exception{
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int count = 0;
		
		try {
			// 커넥션 풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			// SQL문 작성
			sql = "SELECT COUNT(*) FROM zboard_fav WHERE board_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return count;
	}
//	회원 번호와 게시물 번호를 이용한 좋아요 정보
//	(회원이 게시물을 호출했을 때 좋아요 선택 여부 표시)
	public BoardFavVO selectFav(BoardFavVO favVO) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		BoardFavVO fav = null;
		
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM zboard_fav WHERE board_num=? AND mem_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, favVO.getBoard_num());
			pstmt.setInt(2, favVO.getMem_num());
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				fav = new BoardFavVO();
				fav.setBoard_num(rs.getInt("board_num"));
				fav.setMem_num(rs.getInt("mem_num"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return fav;
	}
//	좋아요 삭제
	public void deleteFav(BoardFavVO favVO) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			// 커넥션 풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			// SQL문 작성
			sql = "DELETE FROM zboard_fav WHERE board_num=? AND mem_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, favVO.getBoard_num());
			pstmt.setInt(2, favVO.getMem_num());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
//	내가 선택한 좋아요 목록
	public List<BoardVO> getListBoardFav(int start, int end, int mem_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVO> list = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();
			// SQL 문 작성
			// (주의) zboard_fav의 회원 번호(좋아요  클릭한 회원 번호)로 검색되어야 하기 때문에  f.mem_num으로 표기해야 한다.
			sql= "SELECT * FROM (SELECT a.*, rownum rnum FROM "
					+ "(SELECT * FROM zboard b JOIN "
					+ "zmember m USING(mem_num) JOIN zboard_fav f USING(board_num) "
					+ "WHERE f.mem_num=? ORDER BY board_num DESC)a) WHERE rnum>=? AND rnum <=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mem_num);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			
			rs=pstmt.executeQuery();
			list = new ArrayList<BoardVO>();
			while (rs.next()) {
				BoardVO board = new BoardVO();
				board.setBoard_num(rs.getInt("board_num"));
				board.setTitle(StringUtil.useNoHTML(rs.getString("title")));
				board.setReg_date(rs.getDate("reg_date"));
				board.setId(rs.getString("id"));

				list.add(board);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return list;
	}
	
	
//	댓글 등록
	public void insertReplyBoard(BoardReplyVO boardReply) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn=DBUtil.getConnection();
			sql="INSERT INTO zboard_reply(re_num, re_content,re_ip,mem_num,board_num)VALUES(zreply_seq.nextval,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardReply.getRe_content());
			pstmt.setString(2, boardReply.getRe_ip());
			pstmt.setInt(3, boardReply.getMem_num());
			pstmt.setInt(4, boardReply.getBoard_num());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
//	댓글 개수
	public int getReplyBoardCount(int board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int count = 0;
		try {
			conn=DBUtil.getConnection();
			sql = "SELECT COUNT(*) FROM zboard_reply WHERE board_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return count;
	}
//	댓글 목록
	public List<BoardReplyVO> getListReplyBoard(int start, int end, int board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null; 
		List<BoardReplyVO> list = null;
		try {
			conn=DBUtil.getConnection();
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM "
					+ "(SELECT * FROM zboard_reply JOIN zmember USING(mem_num) "
					+ "WHERE board_num=? ORDER BY re_num DESC)a) WHERE rnum >= ? AND rnum <= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			rs=pstmt.executeQuery();
			list = new ArrayList<BoardReplyVO>();
			while(rs.next()) {
				BoardReplyVO reply= new BoardReplyVO();
				reply.setRe_num(rs.getInt("re_num"));
				// 날짜 -> 1분전, 1시간전 , 1일전 형식의 문자열로 변환
				reply.setRe_date(DurationFromNow.getTimeDiffLabel(rs.getString("re_date")));
				reply.setRe_content(StringUtil.useBrNoHTML(rs.getString("re_content")));
				if(rs.getString("re_modifydate") != null) {
					reply.setRe_modifydate(DurationFromNow.getTimeDiffLabel(rs.getString("re_modifydate")));
				} 
				reply.setBoard_num(rs.getInt("board_num"));
				reply.setId(rs.getString("id"));		// 작성자 id
				reply.setMem_num(rs.getInt("mem_num"));		// 작성자 회원 번호
				
				list.add(reply);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return list;
	}
	
//	댓글 상세(댓글 수정, 삭제시 작성자 회원번호 체크 용도로 사용)
	public BoardReplyVO getReplyBoard(int re_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		BoardReplyVO reply = null;
		try {
			conn=DBUtil.getConnection();
			sql = "SELECT * FROM zboard_reply WHERE re_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, re_num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				reply = new BoardReplyVO();
				reply.setMem_num(rs.getInt("mem_num"));
				reply.setRe_num(rs.getInt("re_num"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return reply;
	}
	
//	댓글 수정
	public void updateReplyBoard(BoardReplyVO reply) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn=DBUtil.getConnection();
			sql ="UPDATE zboard_reply SET re_content=?, re_modifydate=SYSDATE, re_ip=? WHERE re_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reply.getRe_content());
			pstmt.setString(2, reply.getRe_ip());
			pstmt.setInt(3, reply.getRe_num());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
//	댓글 삭제
	public void deleteReplyBoard(int re_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn=DBUtil.getConnection();
			sql="DELETE FROM zboard_reply WHERE re_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, re_num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
}
