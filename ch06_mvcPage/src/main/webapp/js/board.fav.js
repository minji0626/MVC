$(function(){
	/*	======================================
		좋아요 선택 여부와 선택한 총 개수 읽기
		====================================== */
	function selectFav(){
		// 서버와 통신
		$.ajax({
			url:'getFav.do',
			type:'post',
			data:{board_num: $('#output_fav').attr('data-num')},
			dataType:'json',
			success:function(param){
				displayFav(param);
			},
			error:function(){
				alert('네트워크 오류가 발생하였습니다.');
			}
		})
	}
	/*	===============================
		좋아요 등록 및 삭제 이벤트 연결
		=============================== */
	$('#output_fav').click(function(){
		$.ajax({
			url:'writeFav.do',
			type:'post',
			data:{board_num:$('#output_fav').attr('data-num')},
			dataType:'json',
			success:function(param){
			 	if(param.result == 'logout'){
					 alert('로그인 후 사용 가능합니다.');
				 } else if(param.result == 'success'){
					 displayFav(param);
				 } else{
					 alert('좋아요 처리 중에 오류가 발생하였습니다.');
				 }
			},
			error:function(){
				alert('네트워크 오류가 발생하였습니다.');
			}
		})
	});
	
	
/*	================
	좋아요 표시 함수
	================ */
	function displayFav(param){
			let output;
			if(param.status == 'yesFav'){// 좋아요 선택
				output = '../images/fav02.gif';
			} else { // 좋아요 미선택
				output = '../images/fav01.gif';
			}
			// 문서 객체에 설정
			$('#output_fav').attr('src',output);
			$('#output_fcount').text(param.count);
	}
	
	/*	================
		초기 데이터 호출
		================ */
	selectFav();
});