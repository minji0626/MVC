package kr.news.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.controller.Action;
import kr.news.dao.NewsDAO;
import kr.news.vo.NewsVO;

public class DeleteAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		
		NewsVO newsVO = new NewsVO();
		newsVO.setNum(Integer.parseInt(request.getParameter("num")));
		newsVO.setPasswd(request.getParameter("passwd"));
		
		NewsDAO dao = NewsDAO.getInstance();
		NewsVO db_news = dao.getNews(newsVO.getNum());
		
		boolean check = false;
		if(db_news != null) {
			check =  db_news.isCheckedPassword(newsVO.getPasswd());
		}
		if(check) {
			dao.delete(newsVO.getNum());
		}
		request.setAttribute("check", check);
		
		return "/WEB-INF/views/delete.jsp";
	}

}
