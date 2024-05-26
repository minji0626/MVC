<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 탈퇴</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	// 회원 정보 등록 유효성 체크
	$('#delete_form').submit(function(){
		const items = document.querySelectorAll('.input-check');
		for(let i=0; i<items.length; i++){
			if(items[i].value.trim()==''){
				const label = document.querySelector('label[for="'+items[i].id+'"]');
				alert(label.textContent+' 필수 입력');
				 items[i].value = '';
		            items[i].focus();
		            return false;
			}
		} // end of for
		
		if($('#passwd').val()!=$('#cpasswd').val()){
			alert('PW와 PW 확인이 불일치합니다.')
			$('#cpasswd').val('').focus();
			$('#passwd').val('');
			return false;
			}
	}); // end of submit
	

	$('#passwd').keyup(function(){
		$('#cpasswd').val('');
		$('#message_cpasswd').text('');
	});
	
	// 비밀번호와 비밀번호 확인 일치 여부 체크
	$('#cpasswd').keyup(function(){ // keydown에서 keyup으로 변경하여 실시간으로 비교하도록 함
	    if($('#passwd').val() == $('#cpasswd').val()){
	        $('#message_cpasswd').text('PW 일치');
	    } else{
	        $('#message_cpasswd').text('');
	    }
	});

});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>회원 탈퇴</h2>
		<form action="deleteUser.do" id="delete_form" method="post">
			<ul>
				<li>
					<label for="id">ID</label>
					<input type="text" id="id" name="id" class="input-check" maxlength="12" autocomplete="off">
				</li>
				<li>
					<label for="email">E-mail</label>
					<input type="email" id="email" name="email" class="input-check" autocomplete="off">
				</li>
				<li>
					<label for="passwd">PW</label>
					<input type="password" id="passwd" name="passwd" class="input-check" maxlength="12" >
				</li>
				<li>
					<label for="cpasswd">PW 확인</label>
					<input type="password" id="cpasswd" class="input-check" maxlength="12" >
					<span id="message_cpasswd"></span>
				</li>
			</ul>
			
			<div class="align-center">
			<input type="submit" value="회원 탈퇴">
			<input type="button" value="My page" onclick="location.href='myPage.do'">
			</div>
		</form>
</div>
</div>
</body>
</html>