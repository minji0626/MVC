<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>뉴스 목록</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
</head>
<body>
<div class="page-main" id="list">
	<h1>뉴스 목록</h1>
	<div class="align-right">
 		<input type="button" value="뉴스 작성" onclick="location.href='writeForm.do'">
	</div>
	<c:if test="${count == 0}">
	<div class="result-display">
		표시할 게시물이 없습니다.
	</div>
	</c:if>
	<c:if test="${count > 0}">
	<table>
		<tr>
			<th>글번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
		</tr>
		<c:forEach var="newsVO" items="${list}">
		<tr>
			<td>${newsVO.num}</td>
			<td><a href="detail.do?num=${newsVO.num}">${newsVO.title}</a></td>
			<td>${newsVO.writer}</td>
			<td>${newsVO.reg_date}</td>
		</tr>
		</c:forEach>
	</table>
	<div class="align-center">
		${page}
	</div>
	</c:if>
</div>
</body>
</html>
