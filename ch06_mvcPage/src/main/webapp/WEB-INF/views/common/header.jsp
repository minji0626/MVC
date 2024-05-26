<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 공통으로 사용하는 menu 생성하기 --%>
<!-- Header 시작 -->
<div id="main_logo">
	<h1 class="align-center">
		<!-- 매번 같은 경로가 아니기 때문에,명시해주는 것이 좋다. -->
		<a href="${pageContext.request.contextPath}/main/main.do">회원제 게시판</a>
	</h1>
</div>
<div id="main_nav">
	<ul>
		<li>
			<a href="${pageContext.request.contextPath}/board/list.do">게시판</a>
		</li>
		<c:if test="${!empty user_num && user_auth == 9 }">
			<li>
				<a href="${pageContext.request.contextPath}/member/adminList.do">회원 관리</a>
			</li>
		</c:if>
		<c:if test="${!empty user_num }">
			<li>
				<a href="${pageContext.request.contextPath}/member/myPage.do">마이페이지</a>
			</li>
		</c:if>
		<c:if test="${!empty user_num && !empty user_photo}">
			<li class="menu-profile">
				<img alt="프로필 사진" src="${pageContext.request.contextPath}/upload/${user_photo}"
				width="25" height="25" class="my-photo">
			</li>
		</c:if>
		<c:if test="${!empty user_num && empty user_photo}">
			<li class="menu-profile">
				<img alt="프로필 사진" src="${pageContext.request.contextPath}/images/face.png"
				width="25" height="25" class="my-photo">
			</li>
		</c:if>
		<c:if test="${!empty user_num }">
			<li class="menu-logout">
				[<b><span>${user_id}</span></b>]
				<a href="${pageContext.request.contextPath}/member/logout.do">Logout</a>
			</li>
		</c:if>
		<c:if test="${empty user_num }">
			<li>
				<a href="${pageContext.request.contextPath}/member/registerUserForm.do">Register</a>
			</li>
		</c:if>
		<c:if test="${empty user_num }">
			<li>
				<a href="${pageContext.request.contextPath}/member/loginForm.do">Login</a>
			</li>
		</c:if>
	</ul>
</div>
<!-- Header 끝 -->