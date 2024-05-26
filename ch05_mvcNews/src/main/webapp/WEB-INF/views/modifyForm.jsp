<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>뉴스 수정</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
<script type="text/javascript">
	window.onload = function() {
		const myForm = document.getElementById('modify_form')
		myForm.onsubmit = function() {
			const items = document
					.querySelectorAll('.input-check')
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
<h1>뉴스 수정</h1>
<form action="modify.do" id="modify_form" method="post" enctype="multipart/form-data">
	<input type="hidden" value="${newsVO.num}" name="num">
<ul>
	<li>
		<label for="title">기사 제목</label>
		<input type="text" id="title" name="title" size="30" maxlength="50" class="input-check"
		value="${newsVO.title}">
	</li>
	<li>
		<label for="writer">작성자</label>
		<input type="text" id="writer" name="writer" size="10" maxlength="10" class="input-check"
		value="${newsVO.writer}">
	</li>
	<li>
		<label for="passwd">비밀번호</label>
		<input type="password" id="passwd" name="passwd" size="12" maxlength="12" class="input-check">
	</li>
	<li>
		<label for="email">이메일</label>
		<input type="email" id="email" name="email" size="20" maxlength="50" class="input-check"
		value="${newsVO.email}"> 
	</li>
	<li>
		<label for="article">기사 내용</label>
		<textarea rows="5" cols="40" id="article" name="article" class="input-check">${newsVO.article}</textarea>
	</li>
	<li class="image-container">
        <label for="filename">사진</label> 
        <input type="file" id="filename" name="filename" accept="image/gif,image/png,image/jpeg">
        <img src="${pageContext.request.contextPath}/upload/${newsVO.filename}" alt="뉴스 이미지">
    </li>


</ul>
	<div class="align-center">
		<input type="submit" value="등록">
		<input type="button" value="목록" onclick="location.href='list.do'">
	</div>
</form>
</div>
</body>
</html>