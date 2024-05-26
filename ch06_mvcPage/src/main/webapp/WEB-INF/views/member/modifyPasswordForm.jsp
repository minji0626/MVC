<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 수정</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	// 회원 정보 등록 유효성 체크
	$('#password_form').submit(function(){
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
			alert('새로운 PW와 새로운 PW 확인이 불일치합니다.')
			$('#cpasswd').val('').focus();
			$('#passwd').val('');
			return false;
			}
	}); // end of submit
	
	// 새 비밀번호 확인까지 한 후 다시 새 비밀번호를 수정하려고 하면 새 비밀번호 확인을 초기화
	$('#passwd').keyup(function(){
		$('#cpasswd').val('');
		$('#message_cpasswd').text('');
	});
	
	// 새 비밀번호와 새 비밀번호 확인 일치 여부 체크
	$('#cpasswd').keyup(function(){ // keydown에서 keyup으로 변경하여 실시간으로 비교하도록 함
	    if($('#passwd').val() == $('#cpasswd').val()){
	        $('#message_cpasswd').text('새로운 PW 일치');
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
		<h2>비밀번호 수정</h2>
		<form action="modifyPassword.do" id="password_form" method="post">
			<ul>
				<li>
					<label for="id">ID</label>
					<input type="text" id="id" name="id" class="input-check" maxlength="12" autocomplete="off">
				</li>
				<li>
					<label for="origin_passwd">현재 PW</label>
					<input type="password" id="origin_passwd" name="origin_passwd" class="input-check" maxlength="12" >
				</li>
				<li>
					<label for="passwd">새로운 PW</label>
					<input type="password" id="passwd" name="passwd" class="input-check" maxlength="12" >
				</li>
				<li>
					<label for="cpasswd">새로운 PW 확인</label>
					<input type="password" id="cpasswd" class="input-check" maxlength="12" >
					<span id="message_cpasswd"></span>
				</li>
			</ul>
			
			<div class="align-center">
			<input type="submit" value="PW 수정">
			<input type="button" value="My page" onclick="location.href='myPage.do'">
			</div>
		</form>
</div>
</div>
</body>
</html>