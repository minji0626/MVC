package kr.board.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;

import kr.board.dao.BoardDAO;
import kr.board.vo.BoardReplyVO;
import kr.controller.Action;

public class DeleteReplyAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

			// 전송된 데이터 인코딩 타입 지정하기
				request.setCharacterEncoding("UTF-8");
				
				// 댓글 번호 반환
				int re_num = Integer.parseInt(request.getParameter("re_num"));
				
				BoardDAO dao = BoardDAO.getInstance();
				BoardReplyVO db_reply = dao.getReplyBoard(re_num);
				
				HttpSession session = request.getSession();
				Integer user_num = (Integer)session.getAttribute("user_num");
				
				Map<String, String> mapAjax = new HashMap<String, String>();
				
				if(user_num == null) {
					// 로그인 X
					mapAjax.put("result","logout");
				} else if ((user_num != null && user_num == db_reply.getMem_num())) {
					// 로그인한 회원 번호와 작성자 회원 번호 일치
					dao.deleteReplyBoard(re_num);
					mapAjax.put("result", "success");

				} else {
					// 로그인한 회원 번호와 작성자 회원 번호 불일치
					mapAjax.put("result","wrongAccess");
				}
				
				// JSON 데이터로 변환
				ObjectMapper mapper = new ObjectMapper();
				String ajaxData = mapper.writeValueAsString(mapAjax);
						
				request.setAttribute("ajaxData", ajaxData);
						
				return "/WEB-INF/views/common/ajax_view.jsp";
			}

		}

