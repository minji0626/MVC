<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 목록</title>
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
			<h2>회원 목록(관리자 전용)</h2>
			<form action="adminList.do" id="search_form" method="get">
				<ul class="search">
					<li>
						<select name="keyfield">
							<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>ID</option>
							<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>이름</option>
							<option value="3" <c:if test="${param.keyfield==3}">selected</c:if>>E-mail</option>
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
				<input type="button" id="list_btn" value="목록" onclick="location.href='adminList.do'">
				<input type="button" id="list_home_btn" value="홈" onclick="location.href='${pageContext.request.contextPath}/main/main.do'">
			</div>
			<c:if test="${count == 0 }">
			<div class="result-display">
				표시할 회원 정보가 없습니다.
			</div>
			</c:if>
			<c:if test="${count > 0 }">
			<table>
				<tr>
					<th>ID</th>
					<th>이름</th>
					<th>E-mail</th>
					<th>전화번호</th>
					<th>가입일</th>
					<th>등급</th>
				</tr>
				<c:forEach var="member" items="${list }">
				<tr>
					<td>
						<c:if test="${member.auth > 0}">
						<a href="adminUserForm.do?mem_num=${member.mem_num }">${member.id}</a>
						</c:if>
						<c:if test="${member.auth == 0}">
							${member.id}
						</c:if>
					</td>
					<td>
						${member.name}
					</td>
					<td>
						${member.email}
					</td>
					<td>
						${member.phone}
					</td>
					<td>
						${member.reg_date}
					</td>
					<td>
						<c:if test="${member.auth == 0 }">탈퇴</c:if>
						<c:if test="${member.auth == 1 }">정지</c:if>
						<c:if test="${member.auth == 2 }">일반</c:if>
						<c:if test="${member.auth == 9 }">관리</c:if>
					</td>
				</tr>
				</c:forEach>
			</table>
				<div class="align-center">${page}</div>
			</c:if>
		</div>
	</div>
</body>
</html>