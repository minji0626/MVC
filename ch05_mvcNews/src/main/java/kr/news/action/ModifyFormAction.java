package kr.news.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.controller.Action;
import kr.news.dao.NewsDAO;
import kr.news.vo.NewsVO;

public class ModifyFormAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("utf-8");
		
		int num = Integer.parseInt(request.getParameter("num"));
		NewsVO newsVO = new NewsVO();
		NewsDAO dao = NewsDAO.getInstance();
		newsVO = dao.getNews(num);
		
		request.setAttribute("newsVO", newsVO);
		
		return "/WEB-INF/views/modifyForm.jsp";
	}

}
