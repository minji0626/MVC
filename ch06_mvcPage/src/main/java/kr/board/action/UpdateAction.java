package kr.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.FileUtil;

public class UpdateAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num == null) {// 로그인 미실시
			return "redirect:/member/loginForm.do";
		}
		
		// 로그인 된 경우
		// 전송된 데이터 인코딩 타입 지정
		request.setCharacterEncoding("UTF-8");
		// 전송되 데이터 반환
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		BoardDAO dao = BoardDAO.getInstance();
		// 수정 전 데이터
		BoardVO db_board = dao.getBoard(board_num);
		// 로그인한 회원 번호와 작성자 회원 번호 일치 여부 체크하기
		if(user_num != db_board.getMem_num()) {
			//  로그인한 회원 번호와 작성자 회원 번호 불일치
			return "/WEB-INF/views/common/notice.jsp";
		} 
			// 로그인한 회원 번호와 작성자 회원 번호 일치
			BoardVO board = new BoardVO();
			
			board.setBoard_num(board_num);
			board.setTitle(request.getParameter("title"));
			board.setContent(request.getParameter("content"));
			board.setIp(request.getRemoteAddr());
			board.setFilename(FileUtil.createFile(request, "filename"));
			
			dao.updateBoard(board);
			
			if(board.getFilename()!=null && !"".equals(board.getFilename())) {
				// filename이 널이 아니고, 빈칸이 아니라면
				// 새 파일로 교체할 때 원래 파일을 제거
				FileUtil.removeFile(request, db_board.getFilename());
			}
		
			request.setAttribute("notice_msg", "글 수정 완료되었습니다.");
			request.setAttribute("notice_url", request.getContextPath()+"/board/detail.do?board_num="+board_num);
			
		return "/WEB-INF/views/common/alert_view.jsp";
		// redirect:/board/detail.do?board_num="+board_num
	}

}
