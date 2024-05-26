<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Main Page</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
	 <h4>최신 상품</h4>
	 	<div class="image-space">
	 		<c:forEach var="item" items="${itemList}">
	 		<div class="horizontal-area">
	 		<a href="${pageContext.request.contextPath}/item/detail.do?item_num=${item.item_num}">
	 			<img src="${pageContext.request.contextPath}/upload/${item.photo1}">
	 			<span>${item.name }</span>
	 			<br>
	 			<b><fmt:formatNumber value="${item.price}"/>원</b>
	 		</a>
	 		</div>
	 		</c:forEach>
	 		<div class="float-clear">
	 			<hr width="100%" size="1" noshade="noshade">
	 		</div>
	 	</div>
	</div>
</div>
</body>
</html>