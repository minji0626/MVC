package kr.board.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.PagingUtil;

public class ListAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//선택한 페이지 반환
		String pageNum = request.getParameter("pageNum");
		if(pageNum==null) pageNum = "1";
		
		BoardDAO dao = BoardDAO.getInstance();
		int count = dao.getCount();
		
		PagingUtil page = new PagingUtil(
				  Integer.parseInt(pageNum),count,1,10,"list.do");
		
		List<BoardVO> list = null;
		if(count > 0) {
			list = dao.getList(page.getStartRow(), page.getEndRow());
		}
		
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("page", page.getPage());
		
		//JSP 경로 반환
		return "/WEB-INF/views/list.jsp";
	}
}