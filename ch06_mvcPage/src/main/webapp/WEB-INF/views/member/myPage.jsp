<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Page</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#photo_btn').click(function(){
		$('#photo_choice').show();
		$(this).hide();
	}); // end of Click
	
	// 이미지 미리보기
	let photo_path = $('.my-photo').attr('src'); // 처음 화면에 보여지는 이미지 읽기
	
	$('#photo').change(function(){
		let my_photo = this.files[0];
		if(!my_photo){
			// 선택을 취소하면 원래 처음 화면으로 되돌림
			$('.my-photo').attr('src',photo_path);
			return;
		}
		if(my_photo.size > 1024*1024){
			alert(Math.round(my_photo.size/1024) + 'kbytes(1024kbytes까지만 업로드 가능)');
			$('.my-photo').attr('src',photo_path);
			$(this).val(''); // 선택한 파일 정보 지우기
			return;
		}
		// 화면에 이미지 미리보기
		const reader = new FileReader();
		reader.readAsDataURL(my_photo);
		
		reader.onload=function(){
			$('.my-photo').attr('src',reader.result);			
		};
	}); // end of Change
	
	// 이미지 전송하기
	$('#photo_submit').click(function(){
		if($('#photo').val()==''){
			alert('파일을 선택해주세요.');
			$('#photo').focus();
			return;
		}
		// 파일 전송
		const form_data = new FormData();
		// 업로드할 파일은 $('#photo').files[0]을 호출할 수 없음
		// $('#photo')[0].files[0] Ehsms
		// document.getElementById('photo').files[0]
		// 형식으로 호출 가능
		form_data.append('photo',$('#photo')[0].files[0]);
		$.ajax({
			url:'updateMyPhoto.do',
			type:'post',
			data:form_data,
			dataType:'json',
			contentType:false,	// 데이터 객체를 문자열로 바꿀지 설정. true이면 일반 문자
			processData:false, // 해당 타입을 true 로 하면 일반 text로 인식함
			success:function(param){
				if(param.result == 'logout'){
					alert('로그인 후 사용 가능합니다.');
				} else if(param.result=='success'){
					alert('프로필 사진이 수정되었습니다.');
					// 수정된 이미지 정보 저장
					photo_path = $('.my-photo').attr('src');
					$('#photo').val('');
					$('#photo_choice').hide();
					$('#photo_btn').show();	// 수정 버튼 표시
				} else {
					alert('파일 전송에 오류가 발생하였습니다.');
				}
			},
			error: function(){
				alert('네트워크 오류 발생');
			}
		})
		
	}); // end of click
	
	// 이미지 미리보기 취소
	$('#photo_reset').click(function(){
		// 초기 이미지 표시
		$('.my-photo').attr('src',photo_path); // 이미지 미리보기 전 이미지로 되돌리기
		$('#photo').val('');
		$('#photo_choice').hide();
		$('#photo_btn').show(); // 수정 버튼 표시
	}) // end of click
});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
	<h2>My Page</h2>
	<div class="mypage-div">
		<h3>Profile Photo</h3>
		<ul>
			<li>
				<c:if test="${empty member.photo}">
					<img alt="profile photo" src="${pageContext.request.contextPath}/images/face.png"
						width="200" height="200" class="my-photo">
				</c:if>
				<c:if test="${!empty member.photo}">
					<img alt="profile photo" src="${pageContext.request.contextPath}/upload/${member.photo}"
						width="200" height="200" class="my-photo">
				</c:if>
			</li>
			<li>
				<div class="align-center">
					<input type="button" value="수정" id="photo_btn">
				</div>
				<div id="photo_choice" style="display: none;">
					<input type="file" id="photo" accept="image/gif,image/png,image/jpeg">
					<input type="button" value="전송" id="photo_submit">
					<input type="button" value="취소" id="photo_reset">
				</div>
			</li>
		</ul>
		<h3>연락처 <input type="button" id="tel_btn" value="연락처 수정" onclick="location.href='modifyUserForm.do'"> </h3>
		
		<ul>
			<li>ID : ${member.id }</li>
			<li>이름 : ${member.name }</li>
			<li>전화번호 : ${member.phone }</li>
			<li>E-mail : ${member.email }</li>
			<li>우편번호 : ${member.zipcode }</li>
			<li>주소 : ${member.address1} ${member.address2}</li>
			<li>가입일 : ${member.reg_date }</li>
			<c:if test="${!empty member.modify_date }">
				<li>최근 정보 수정일 : ${member.modify_date }</li>
			</c:if>		
		</ul>
		<h3>비밀번호 수정 <input type="button" id="passwd_btn" value="비밀번호 변경" onclick="location.href='modifyPasswordForm.do'"> </h3>
		<h3>회원 탈퇴 <input type="button" id="delete_btn" value="회원 탈퇴" onclick="location.href='deleteUserForm.do'">  </h3>
	</div>
	
	<div class="mypage-div">
		<h3>관심 게시물 목록</h3>
		<table>
			<tr>
				<th>제목</th>
				<th>작성자</th>
				<th>등록일</th>
			</tr>
			<c:forEach var="board" items="${boardList}">
				<tr>
					<td><a href="${pageContext.request.contextPath}/board/detail.do?board_num=${board.board_num }" target="_blank">${fn:substring(board.title,0,12)}</a></td>
					<td>${board.id}</td>
					<td>${board.reg_date}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	
	<div class="mypage-end">
	</div>
 </div>
</div>
</body>
</html>