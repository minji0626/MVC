<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>새 글 작성</title> 
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
<script type="text/javascript">
	window.onload = function() {
		const myForm = document.getElementById('register_form')
		myForm.onsubmit = function() {
			const items = document
					.querySelectorAll('input[type="text"],input[type="password"],input[type="email"],textarea')
			for (let i = 0; i < items.length; i++) {
				if (items[i].value.trim() == '') {
					const label = document.querySelector('label[for="'+ items[i].id + '"]');
					alert(label.textContent + ' 항목은 필수 입력하셔야 합니다.');
					items[i].value = '';
					items[i].focus();
					return false;
				} // end of if
			} // end of for
		};
	};
</script>

</head>
<body>
<div class="page-main"> 
<h2>새 글 작성</h2>
<form action="write.do" id="register_form" method="post">
<ul>
	<li>
		<label for="title">제목</label>
		<input type="text" id="title" name="title" size="10" maxlength="50">
	</li>
	<li>
		<label for="name">작성자</label>
		<input type="text" id="name" name="name" size="10" maxlength="10">
	</li>
	<li>
		<label for="passwd">비밀번호</label>
		<input type="password" id="passwd" name="passwd" size="12" maxlength="12">
	</li>
	<li>
		<label for="email">이메일</label>
		<input type="email" id="email" name="email" size="20" maxlength="50">
	</li>
	<li>
		<label for="content">내용</label>
		<textarea rows="5" cols="40" id="content" name="content"></textarea>
	</li>
</ul>
	<div class="align-center">
		<input type="submit" value="글 등록">
		<input type="button" value="목록" onclick="location.href='list.do'">
	</div>
</form>
</div>
</body>
</html>