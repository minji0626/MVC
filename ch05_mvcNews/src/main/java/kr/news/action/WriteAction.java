package kr.news.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.controller.Action;
import kr.news.dao.NewsDAO;
import kr.news.vo.NewsVO;
import kr.util.FileUtil;

public class WriteAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("utf-8");
		
		NewsVO newsVO = new NewsVO();
		newsVO.setTitle(request.getParameter("title"));
		newsVO.setArticle(request.getParameter("article"));
		newsVO.setEmail(request.getParameter("email"));
		newsVO.setPasswd(request.getParameter("passwd"));
		newsVO.setWriter(request.getParameter("writer"));
														// 파라미터 명
		newsVO.setFilename(FileUtil.createFile(request,"filename"));
		
		NewsDAO dao = NewsDAO.getInstance();
		dao.registerNews(newsVO);
		
		return "/WEB-INF/views/write.jsp";
	}

}
