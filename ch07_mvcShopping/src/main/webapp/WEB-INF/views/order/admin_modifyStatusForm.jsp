<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>배송상태 변경(관리자전용)</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>배송상태 변경(관리자전용)</h2>
		<form action="modifyStatus.do" method="post"
		                                 id="status_modify">
			<input type="hidden" name="order_num" 
			                    value="${order.order_num}"> 
			<ul>
				<li>
					<label>배송상태</label>
					<c:if test="${order.status != 5}">
					<input type="radio" name="status" id="status1" value="1"
					 <c:if test="${order.status==1}">checked</c:if>>배송대기
					<input type="radio" name="status" id="status2" value="2"
					 <c:if test="${order.status==2}">checked</c:if>>배송준비중 
					<input type="radio" name="status" id="status3" value="3"
					 <c:if test="${order.status==3}">checked</c:if>>배송중 
					<input type="radio" name="status" id="status4" value="4"
					 <c:if test="${order.status==4}">checked</c:if>>배송완료 
					</c:if>
					<input type="radio" name="status" id="status5" value="5"
					 <c:if test="${order.status==5}">checked</c:if>>주문취소
				</li>
			</ul>
			<div class="align-center">
				<c:if test="${order.status != 5}">
				<input type="submit" value="수정">
				</c:if>
				<input type="button" value="주문상세"
				 onclick="location.href='adminDetail.do?order_num=${order.order_num}'">
			</div>                                                    
		</form>
	</div>
</div>
</body>
</html>






