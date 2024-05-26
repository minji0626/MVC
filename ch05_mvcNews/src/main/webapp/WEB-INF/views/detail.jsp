<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="kr.news.dao.NewsDAO"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>뉴스 상세 정보</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
</head>
<body>
<div class="page-main">
    <h1>${newsVO.num}번 뉴스 보기</h1>
    <ul id="detail_page">
        <li id="title">${newsVO.title}</li>

        <li id="writer">작성자 : ${newsVO.writer}<br>${newsVO.email}</li>
    </ul>
    <hr width="100%" size="1" noshade="noshade">
    <p><img src="${pageContext.request.contextPath}/upload/${newsVO.filename}" alt="뉴스 이미지"></p>
    <p>${newsVO.article}</p>
    <hr width="100%" size="1" noshade="noshade">
    
    <div class="align-right" id="detail_btn">
        작성일 : ${newsVO.reg_date}
		<input type="button" value="수정" onclick="location.href='modifyForm.do?num=${newsVO.num}'">
		<input type="button" value="삭제" onclick="location.href='deleteForm.do?num=${newsVO.num}'">
		<input type="button" value="목록" onclick="location.href='list.do'">
    </div>
</div>
</body>
</html>
