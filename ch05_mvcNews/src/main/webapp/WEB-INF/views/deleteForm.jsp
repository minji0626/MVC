<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int num = Integer.parseInt(request.getParameter("num"));
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>뉴스 삭제</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap"
	rel="stylesheet">
<script type="text/javascript">
window.onload=function(){
	const myForm = document.getElementById('delete_form');
	// 이벤트 연결
	myForm.onsubmit=function(){
		const passwd = document.getElementById('passwd');
		if(passwd.value.trim()==''){
			alert('비밀번호를 입력하세요');
			passwd.value='';
			passwd.focus();
			return false;
		}
	}
}
</script>
</head>
<body>
<div class="page-main">
	<h2>뉴스 삭제</h2>
	<form action="delete.do" method="post" id="delete_form">
			<input type="hidden" name="num" value="${num}">
			<ul>
				<li>
				<label for="passwd">비밀번호</label>
				<input type="password" name="passwd" id="passwd" size="12" maxlength="12">
				</li>
			</ul>
			<div class="align-center">
				<input type="submit" value="삭제">
				<input type="button" value="목록" onclick="location.href='list.do'">
			</div>
		</form>
</div>	
</body>
</html>