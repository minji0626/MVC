<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 상세 정보</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/board.fav.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/board.reply.js"></script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
    <h2>${board.title}</h2>
    <ul class="detail-info">
        <li>
        	<c:if test="${!empty board.photo }">
        		<img class="my-photo" width="40" height="40" src="${pageContext.request.contextPath}/upload/${board.photo}" alt="프로필 이미지">
        	</c:if>
        	<c:if test="${empty board.photo }">
        		<img class="my-photo" width="40" height="40" src="${pageContext.request.contextPath}/images/face.png" alt="기본 이미지">
        	</c:if>
        </li>
        <li class="info">
        	${board.id}<br>
        	조회 : ${board.hit}
        </li>
    </ul>
    <hr width="100%" size="1" noshade="noshade">
    		<c:if test="${!empty board.filename }">
    			<div class="align-center">
        		<img src="${pageContext.request.contextPath}/upload/${board.filename}">
        		</div>
        	</c:if>
        		<p>${board.content}</p>
    <hr width="100%" size="1" noshade="noshade">
    <ul class="detail-sub">  
    	<li>
    		<%-- 좋아요 --%>
    		<img id="output_fav" data-num="${board.board_num}" src="${pageContext.request.contextPath}/images/fav01.gif" width="50" >
    		좋아요
    		<span id="output_fcount"></span>
    	</li>
    	<li>
    		<c:if test="${!empty board.modify_date }">
        		최근 수정일 : ${board.modify_date }
        	</c:if>
        	
        	<%-- 로그인한 회원 번호와 작성자 회원 번호가 일치해야 수정, 삭제 가능함 --%>
        	<div id="detail_btn">
        	작성일 : ${board.reg_date}
        		<c:if test="${user_num == board.mem_num }">
        			<input type="button" value="수정" onclick="location.href='updateForm.do?board_num=${board.board_num}'">
					<input type="button" value="삭제" id="delete_btn">
						<script type="text/javascript">
							const delete_btn = document.getElementById('delete_btn');
							delete_btn.onclick = function(){
								let choice = confirm('해당 글을 삭제하시겠습니까?');
								if(choice){
									location.replace('delete.do?board_num=${board.board_num}');
								}
							}
						</script>
        		</c:if>
        			<input type="button" value="목록" onclick="location.href='list.do'">
        	</div>
    	</li>
    </ul>
    
    <!-- 댓글 시작 -->
    <div id="reply_div">
    	<span class="re-title">댓글 달기</span>
    	<form id="re_form">
    		<input type="hidden" name="board_num" value="${board.board_num }" id="board_num">
    		<textarea rows="3" cols="50" name="re_content"
    		<c:if test="${empty user_num }">disabled="disabled"</c:if>
    		 id="re_content" class="rep=content"><c:if test="${empty user_num }">로그인 후 작성 가능합니다.</c:if></textarea>
    		 <c:if test="${!empty user_num }">
				<div id="re_first">
				 <span class="letter-count">300/300</span>
				</div>
				<div id="re_second" class="align-right">
					<input type="submit" value="전송">
				</div>
			</c:if>
    	</form>
    </div>
    <!-- 댓글 목록 출력 시작 -->
    <div id="output">
    	
    </div>
    <div class="paging-button" style="display: none;">
    		<input type="button" value="다음 글 보기">
    </div>
    <div id="loading" style="display: none;">
    	<img src="${pageContext.request.contextPath}/images/loading.gif" width="50" height="50">
    </div>
    <!-- 댓글 목록 출력 끝 -->
    <!-- 댓글 끝 -->
    </div>
</div>
</body>
</html>
