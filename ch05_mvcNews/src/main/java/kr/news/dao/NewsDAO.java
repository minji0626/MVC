package kr.news.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.news.vo.NewsVO;
import kr.util.DBUtil;

public class NewsDAO {
	// 싱글턴 패턴
	private static NewsDAO instance = new NewsDAO();
	
	public static NewsDAO getInstance() {
		return instance;
	}
	private NewsDAO() {
		
	}
	
	// 뉴스 등록
	public void registerNews(NewsVO newsVO) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql =  null;
		try {
			conn = DBUtil.getConnection();
			sql="INSERT INTO dailynews(num,title,writer,passwd,email,article,filename)"
					+ "VALUES(dailynews_seq.nextval,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newsVO.getTitle());
			pstmt.setString(2, newsVO.getWriter());
			pstmt.setString(3, newsVO.getPasswd());
			pstmt.setString(4, newsVO.getEmail());
			pstmt.setString(5, newsVO.getArticle());
			pstmt.setString(6, newsVO.getFilename());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	// 뉴스 총 개수
	public int getCount() throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql =  null;
		int count = 0;
		try {
			conn = DBUtil.getConnection();
			sql="SELECT COUNT(*) FROM dailynews";
			pstmt=conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				// 컬럼 인덱스를 사용하면 된다.하나만 있으면 어차피 1이기 때문에
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}
	
	// 뉴스 목록
	public List<NewsVO> getList(int startRow, int endRow) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<NewsVO> list = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();
			sql="SELECT * FROM (SELECT ROWNUM rnum,a.* FROM (SELECT * FROM dailynews order by num asc )a) WHERE rnum>=? and rnum<=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			list = new ArrayList<NewsVO>();
			while (rs.next()) {
				NewsVO newsVO = new NewsVO();
				newsVO.setNum(rs.getInt("num"));
				newsVO.setTitle(rs.getString("title"));
				newsVO.setWriter(rs.getString("writer"));
				newsVO.setReg_date(rs.getDate("reg_date"));

				list.add(newsVO);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}
	
	// 뉴스 상세
	public NewsVO getNews(int num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		NewsVO news = null;
		try {
			conn = DBUtil.getConnection();
			sql="SELECT * FROM dailynews WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				news = new NewsVO();
				news.setNum(num);
				news.setArticle(rs.getString("article"));
				news.setTitle(rs.getString("title"));
				news.setWriter(rs.getString("writer"));
				news.setPasswd(rs.getString("passwd"));
				news.setFilename(rs.getString("filename"));
				news.setReg_date(rs.getDate("reg_date"));
				news.setEmail(rs.getString("email"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return news;
	}
	
//	뉴스 수정
	public void update(NewsVO newsVO) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql =  null;
		String sub_sql = "";
		int cnt = 0;
		try {
			conn = DBUtil.getConnection();
			if(newsVO.getFilename()!= null && !newsVO.getFilename().isEmpty()) {
				sub_sql +=", filename=?";
			}
			sql="UPDATE dailynews SET title=?, writer=?,email=?,article=?"+sub_sql+" WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++cnt, newsVO.getTitle());
			pstmt.setString(++cnt, newsVO.getWriter());
			pstmt.setString(++cnt, newsVO.getEmail());
			pstmt.setString(++cnt, newsVO.getArticle());
			if(newsVO.getFilename()!= null && !newsVO.getFilename().isEmpty()) {
				// 파일이 업로드가 되었다면 ++cnt의 값은 5번, num => 6번이 될것이다
				pstmt.setString(++cnt, newsVO.getFilename());
			}
			// 파일의 업로드가 없다면 if문이 실행되지 않기 때문에 번호가 5번이 된다.
			pstmt.setInt(++cnt, newsVO.getNum());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
//	뉴스 삭제
	public void delete(int num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql =  null;
		try {
			conn = DBUtil.getConnection();
			sql="DELETE FROM dailynews WHERE num = ?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
}
