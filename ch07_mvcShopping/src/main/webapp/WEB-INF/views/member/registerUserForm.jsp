<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register Form</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	let idChecked = 0; // 0: 중복, id 중복 체크 미실시, 1: 미중복
	
	// 아이디 중복 체크
	$('#id_check').click(function(){
		if(!/^[A-Za-z0-9]{4,12}$/.test($('#id').val())){
			alert('영문 또는 숫자 사용, 최소 4자 ~ 최대 12자 사용');
			$('#id').val('').focus();
			return;
		}
		
		// 서버와 통신
		$.ajax({
			url:'checkDuplicatedId.do',
			type:'post',
			data:{id:$('#id').val()},
			dataType: 'json',
			success: function(param){
				if(param.result=='idNotFound'){
					idChecked = 1;
					$('#message_id').css('color','black').text('등록 가능 ID');
				}else if(param.result=='idDuplicated'){
					idChecked = 0;
					$('#message_id').css('color','red').text('중복된 ID');
					$('#id').val('').focus();
				} else{
					idChecked=0;
					alert('ID 중복 체크 오류 발생');
				}
			},
			error:function(){
				idChecked = 0;
				alert('Network Error Occurred');
			}
		})
		
	});// end of Click
	
	// 아이디 중복 안내 메시지 초기화 및 아이디 중복 값 초기화
	$('#register_form #id').keydown(function(){
		idChecked = 0;
		$('#message_id').text('');
		
	});// end of Keydown
	
	// 회원 정보 등록 유효성 체크
	$('#register_form').submit(function(){
		const items = document.querySelectorAll('.input-check');
		for(let i=0; i<items.length; i++){
			if(items[i].value.trim()==''){
				const label = document.querySelector('label[for="'+items[i].id+'"]');
				alert(label.textContent+' 필수 입력');
				 items[i].value = '';
		            items[i].focus();
		            return false;
		        } // if 4
			
		        if(items[i].id == 'id' && !/^[A-Za-z0-9]{4,12}$/.test($('#id').val())){
		            alert('영문 또는 숫자 사용, 최소 4자 ~ 최대 12자 사용');
		            $('#id').val('').focus();
		            return false;
		       } // if 2
		       
		        if(items[i].id == 'id' && idChecked == 0){
		            alert('ID 중복 체크는 필수입니다.');
		            return false;
		        } // if 3
		        
		        if(items[i].id == 'zipcode' && !/^[0-9]{5}$/.test($('#zipcode').val())){
		        	alert('우편 번호를 입력하세요(숫자 5자리)');
		        	$('#zipcode').val('').focus();
		            return false;
		        }
		}
	}); // end of submit
});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>Register Form</h2>
		<form action="registerUser.do" id="register_form" method="post">
			<ul>
				<li>
					<label for="id">ID</label>
					<input type="text" id="id" name="id" class="input-check" maxlength="12" autocomplete="off">
					<input type="button" value="Confirm ID" id="id_check">
					<span id="message_id"></span>
					<div class="form-notice">* English/Number(4~12)</div>
				</li>
				<li>
					<label for="name">이름</label>
					<input type="text" id="name" name="name" class="input-check" maxlength="10" >
				</li>
				<li>
					<label for="passwd">PW</label>
					<input type="password" id="passwd" name="passwd" class="input-check" maxlength="12" >
				</li>
				<li>
					<label for="phone">전화 번호</label>
					<input type="text" id="phone" name="phone" class="input-check" maxlength="15" >
				</li>
				<li>
					<label for="email">E-mail</label>
					<input type="email" id="email" name="email" class="input-check" maxlength="50" >
				</li>
				<li>
					<label for="zipcode">우편 번호</label>
					<input type="text" id="zipcode" name="zipcode" class="input-check" maxlength="5" autocomplete="off">
					<input type="button" value="우편번호 찾기" onclick="execDaumPostcode()">
				
				</li>
				<li>
					<label for="address1">주소</label>
					<input type="text" id="address1" name="address1" class="input-check" maxlength="30" >
				</li>
				<li>
					<label for="address2">상세 주소</label>
					<input type="text" id="address2" name="address2" class="input-check" maxlength="30" >
				</li>
			</ul>
			
			<div class="align-center">
			<input type="submit" value="등록">
			<input type="button" value="메인" onclick="location.href='${pageContext.request.contextPath}/main/main.do'">
			</div>
			
		</form>
		
		<!-- 다음 우편번호 API 시작 -->
			<!-- iOS에서는 position:fixed 버그가 있음, 적용하는 사이트에 맞게 position:absolute 등을 이용하여 top,left값 조정 필요 -->
			<div id="layer"
				style="display: none; position: fixed; overflow: hidden; z-index: 1; -webkit-overflow-scrolling: touch;">
				<img src="//t1.daumcdn.net/postcode/resource/images/close.png"
					id="btnCloseLayer"
					style="cursor: pointer; position: absolute; right: -3px; top: -3px; z-index: 1"
					onclick="closeDaumPostcode()" alt="닫기 버튼">
			</div>

			<script
				src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
			<script>
    // 우편번호 찾기 화면을 넣을 element
    var element_layer = document.getElementById('layer');

    function closeDaumPostcode() {
        // iframe을 넣은 element를 안보이게 한다.
        element_layer.style.display = 'none';
    }

    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    //(주의)address1에 참고항목이 보여지도록 수정
                    // 조합된 참고항목을 해당 필드에 넣는다.
                    //(수정) document.getElementById("address2").value = extraAddr;
                
                } 
                //(수정) else {
                //(수정)    document.getElementById("address2").value = '';
                //(수정) }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('zipcode').value = data.zonecode;
                //(수정) + extraAddr를 추가해서 address1에 참고항목이 보여지도록 수정
                document.getElementById("address1").value = addr + extraAddr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("address2").focus();

                // iframe을 넣은 element를 안보이게 한다.
                // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
                element_layer.style.display = 'none';
            },
            width : '100%',
            height : '100%',
            maxSuggestItems : 5
        }).embed(element_layer);

        // iframe을 넣은 element를 보이게 한다.
        element_layer.style.display = 'block';

        // iframe을 넣은 element의 위치를 화면의 가운데로 이동시킨다.
        initLayerPosition();
    }

   	 // 브라우저의 크기 변경에 따라 레이어를 가운데로 이동시키고자 하실때에는
   	 // resize이벤트나, orientationchange이벤트를 이용하여 값이 변경될때마다 아래 함수를 실행 시켜 주시거나,
   	 // 직접 element_layer의 top,left값을 수정해 주시면 됩니다.
    function initLayerPosition(){
        var width = 300; //우편번호서비스가 들어갈 element의 width
        var height = 400; //우편번호서비스가 들어갈 element의 height
        var borderWidth = 5; //샘플에서 사용하는 border의 두께

        // 위에서 선언한 값들을 실제 element에 넣는다.
        element_layer.style.width = width + 'px';
        element_layer.style.height = height + 'px';
        element_layer.style.border = borderWidth + 'px solid';
        // 실행되는 순간의 화면 너비와 높이 값을 가져와서 중앙에 뜰 수 있도록 위치를 계산한다.
        element_layer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width)/2 - borderWidth) + 'px';
        element_layer.style.top = (((window.innerHeight || document.documentElement.clientHeight) - height)/2 - borderWidth) + 'px';
    }
</script>
<!-- 다음 우편번호 API 끝 -->
</div>
</div>
</body>
</html>