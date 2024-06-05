<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>배송 상태 변경 - 관리자</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">

</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>배송 상태 변경 - 관리자</h2>
		<form action="modifyStatus.do" method="post" id="status_modify">
			<input type="hidden" name="order_num" value="${order.order_num }">
			<ul>
			<li>
				<label>배송 상태</label>
				<c:if test="${order.status != 5 }">
					<input type="radio" name="status" id="status1"
					<c:if test="${order.status == 1 }">checked</c:if>> 배송 대기
					<input type="radio" name="status" id="status2"
					<c:if test="${order.status == 2 }">checked</c:if>> 배송 준비 중
					<input type="radio" name="status" id="status3"
					<c:if test="${order.status == 3 }">checked</c:if>> 배송중
					<input type="radio" name="status" id="status4"
					<c:if test="${order.status == 4 }">checked</c:if>> 배송 완료
				</c:if>
				<input type="radio" name="status" id="status5"
				<c:if test="${order.status == 5 }">checked</c:if>> 주문 취소
			</li>
			</ul>
			
			<div class="align-center">
			
			</div>
			
		</form>
	</div>
</div>
</body>
</html>