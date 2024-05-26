<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="kr.board.dao.BoardDAO" %>
<%@ page import="kr.board.vo.BoardVO" %>
<%@ page import="java.util.List" %>
<%@ page import="kr.util.PagingUtil"%>

<%
	// 선택한 페이지 번호
	String pageNum = request.getParameter("pageNum");
	// 최초에 list.jsp를 호출하면 pageNum을 전달할 수 없기 때문에 null이 되고 연산할 때 연산이 되지 않는 문제가 있어서 최초 호출시 무조건 1페이지로 설정
	if(pageNum == null){
		pageNum="1";
	}
	// 한 화면에 몇 개의 글(행,레코드)를 보여줄지 지정
	int rowCount = 10;
	// 한 화면에 몇 개의 페이지 수를 보여줄 지를 지정
	int pageCount = 10;
	
	// 현재 선택한 페이지 String -> int
	int currentPage = Integer.parseInt(pageNum);
	
	BoardDAO dao = BoardDAO.getInstance();
	int count = dao.getCount();
	
	PagingUtil util = new PagingUtil(currentPage,count,rowCount,pageCount,"list.do");
	
	List<BoardVO> list = null;
	if(count > 0){
		list = dao.getList(util.getStartRow(), util.getEndRow());
	}

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>목록</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
</head>
<body>
<div class="page-main" id="list">
	<h2>게시판 목록</h2>
	<div class="align-right">
 		<input type="button" value="새 글 작성" onclick="location.href='writeForm.do'">
	</div>
	<%
	if(count == 0){
%>
	<div class="result-display">저장된 글이 없습니다.</div>
<%	} 
	else{
%>
	<!-- 목록 출력 시작 -->
	<table>
		<tr>
			<th>글 번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
		</tr>
<%
	for(BoardVO boardVO : list){
%>
		<tr>
			<td><%=boardVO.getNum() %></td>
			<td><a href="detail.do?num=<%=boardVO.getNum()%>"><%=boardVO.getTitle() %></a></td>
			<td><%=boardVO.getName() %></td>
			<td><%=boardVO.getReg_date() %></td>
		</tr>
<%
	}
%>
	
	</table>
<%} %>
	<!-- 목록 출력 끝 -->
	<!-- 페이지 표시 시작 -->
	<div class="align-center">
	<%= util.getPage() %>
	</div>
	<!-- 페이지 표시 끝 -->
</div>
</body>
</html>