<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${check }">
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 탈퇴</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="result-display">
			<div class="align-center">
				회원 탈퇴가 완료되었습니다.<br>
				<p>
				<input type="button" value="메인" onclick="location.href='${pageContext.request.contextPath}/main/main.do'">
			</div>
		</div>
</div>
</body>
</html>
</c:if>

<c:if test="${!check }">
	<script type="text/javascript">
		alert('입력한 정보가 정확하지 않습니다.');
		history.go(-1);
	</script>
</c:if>
