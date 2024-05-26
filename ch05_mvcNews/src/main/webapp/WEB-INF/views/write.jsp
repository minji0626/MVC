<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>뉴스 등록 완료</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
</head>
<body>
	<div class="page-main">
		<h1>뉴스 작성 완료</h1>
		<div class="result-display">
			<div class="align-center">
			 	뉴스가 정상적으로 등록되었습니다.
				<p>
				<button onclick="location.href='list.do'">목록</button>
			</div>
		</div>
	</div>
</body>
</html>