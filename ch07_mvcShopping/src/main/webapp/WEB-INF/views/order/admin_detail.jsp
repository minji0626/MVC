<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>구매상세(관리자 전용)</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>구매상세 - 관리자</h2>
			<table>
				<tr>
					<th>상품명</th>
					<th>수량</th>
					<th>상품가격</th>
					<th>합계</th>
				</tr>
				<c:forEach var="detail" items="${detailList}">
				<tr>
					<td>${detail.item_name}</td>
					<td class="align-center">
						<fmt:formatNumber value="${detail.order_quantity}"/>
					</td>
					<td class="align-center">
						<fmt:formatNumber value="${detail.item_price}"/>원
					</td>
					<td class="align-center">
						<fmt:formatNumber value="${detail.item_total}"/>원
					</td>
				</tr>
				</c:forEach>
				<tr>
					<td colspan="3" class="align-right"><b>총구매금액</b></td>
					<td class="align-center"><fmt:formatNumber value="${order.order_total}"/>원</td>
				</tr>
			</table>    
			<ul id="delivery_info">
				<li>
					<span>받는 사람</span> ${order.receive_name}
				</li>
				<li>
					<span>우편번호</span> ${order.receive_post}
				</li>
				<li>
					<span>주소</span> ${order.receive_address1} ${order.receive_address2}
				</li>
				<li>
					<span>전화번호</span> ${order.receive_phone}
				</li>
				<li>
					<span>남기실 말씀</span> ${order.notice}
				</li>
				<li>
					<span>결제수단</span>
					<c:if test="${order.payment == 1}">통장입금</c:if> 
					<c:if test="${order.payment == 2}">카드결제</c:if> 
				</li>
				<li>
					<span>배송상태</span>
					<c:if test="${order.status == 1}">배송대기</c:if>
					<c:if test="${order.status == 2}">배송준비중</c:if>
					<c:if test="${order.status == 3}">배송중</c:if>
					<c:if test="${order.status == 4}">배송완료</c:if>
					<c:if test="${order.status == 5}">주문취소</c:if>
				</li>
				<li class="align-center">
					<c:if test="${order.status == 1}">
					<input type="button" value="배송지 정보 수정" 
					   onclick="location.href='modifyForm.do?order_num=${order.order_num}'"> 
					</c:if>
					<c:if test="${order.status != 5}">
					<input type="button" value="배송 상태 수정" 
					   onclick="location.href='modifyStatusForm.do?order_num=${order.order_num}'"> 
					</c:if>
					<c:if test="${order.status == 4 or order.status == 5}">
					<input type="button" value="삭제" 
					   onclick="location.href='deleteOrder.do?order_num=${order.order_num}'"> 
					</c:if>
					<input type="button" value="주문 목록" 
					   onclick="location.href='adminList.do'">
				</li>
			</ul>			
	</div>
</div>	
</body>
</html>






