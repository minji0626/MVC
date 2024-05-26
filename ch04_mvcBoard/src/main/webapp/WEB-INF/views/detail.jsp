<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="kr.board.dao.BoardDAO"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 상세 정보</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
</head>
<body>
<div class="page-main">
    <h2>글 상세 보기</h2>
    <ul id="detail_page">
        <li>글 번호 : ${boardVO.num}</li>
        <li>제목 : ${boardVO.title}</li>
        <li>작성자 : ${boardVO.name}</li>
        <li>이메일 : ${boardVO.email}</li>
    </ul>
    <hr width="100%" size="1" noshade="noshade">
    <p>${boardVO.content}</p>
    <hr width="100%" size="1" noshade="noshade">
    
    <div class="align-right" id="detail_btn">
        작성일 : ${boardVO.reg_date}
		<input type="button" value="수정" onclick="location.href='modifyForm.do?num=${boardVO.num}'">
		<input type="button" value="삭제" onclick="location.href='deleteForm.do?num=${boardVO.num}'">
		<input type="button" value="목록" onclick="location.href='list.do'">
    </div>
</div>
</body>
</html>
