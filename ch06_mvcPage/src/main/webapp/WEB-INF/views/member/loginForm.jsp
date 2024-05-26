<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#login_form').submit(function(){
		if($('#id').val().trim()==''){
			alert('아이디를 입력하세요');
			$('#id').val('').focus();
			return false;
		}
		if($('#passwd').val().trim()==''){
			alert('비밀번호를 입력하세요');
			$('#passwd').val('').focus();
			return false;
		}
	})
});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>Login</h2>
	</div>
	<form action="login.do" id="login_form" method="post">
		<ul>
			<li class="floating-label">
				<input type="text" class="form-input" placeholder="ID" name="id" id="id" maxlength="12" autocomplete="off">
				<label for="id">ID</label>
			</li>
			<li class="floating-label">
				<input type="password" class="form-input" placeholder="PW" name="passwd" id="passwd" maxlength="12">
				<label for="passwd">PW</label>
			</li>
		</ul>
		<div class="align-center">
		<input type="submit" value="Login"> 
		<input type="button" value="Home" onclick="location.href='${pageContext.request.contextPath}/main/main.do'">
		</div>
	</form>
</div>
</body>
</html>