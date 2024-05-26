<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 목록</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	window.onload = function(){
		const myForm = document.getElementById('search_form');
		myForm.onsubmit = function(){
			if(keyword.value.trim()==''){
				const keyword = document.getElementById('keyword');
					alert('검색어를 입력하세요.');
					keyword.value='';
					keyword.focus();
					return false;
			};
		};
	};
</script>
</head>
<body>
	<div class="page-main">
		<jsp:include page="/WEB-INF/views/common/header.jsp" />
		<div class="content-main">
			<h2>게시판 목록</h2>
			<form id="search_form" action="list.do" method="get">
				<ul class="search">
					<li>
						<select name="keyfield">
							<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>제목</option>
							<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>작성자</option>
							<option value="3" <c:if test="${param.keyfield==3}">selected</c:if>>내용</option>
						</select>
					</li>
					<li>
						<input type="search" size="16" name="keyword" id="keyword" value="${param.keyword }">
					</li>
					<li>
						<input type="submit" id="find_btn" value="찾기">
					</li>
				</ul>
			</form>
			<div class="list-space align-right">
				<input type="button" id="write_btn" value="새 글 작성" onclick="location.href='writeForm.do'" 
				<c:if test="${empty user_num }">disabled="disabled"</c:if>
				>
				<input type="button" id="list_btn" value="목록" onclick="location.href='list.do'">
				<input type="button" id="list_home_btn" value="홈" onclick="location.href='${pageContext.request.contextPath}/main/main.do'">
			</div>
			<c:if test="${count == 0 }">
			<div class="result-display">
				표시할 글 정보가 없습니다.
			</div>
			</c:if>
			<c:if test="${count > 0 }">
			<table>
				<tr>
					<th>글 번호</th>
					<th>제목</th>
					<th>작성자</th>
					<th>작성일</th>
					<th>조회</th>
				</tr>
				<c:forEach var="board" items="${list }">
				<tr>
					<td>${board.board_num }</td>
					<td>
						<a href="detail.do?board_num=${board.board_num }">${board.title}</a>
					</td>
					<td>${board.id}</td>
					<td>${board.reg_date}</td>
					<td>${board.hit}</td>
				</tr>
				</c:forEach>
			</table>
				<div class="align-center">${page}</div>
			</c:if>
		</div>
	</div>
</body>
</html>