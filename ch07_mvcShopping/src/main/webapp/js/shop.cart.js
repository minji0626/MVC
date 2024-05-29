$(function(){
	/*
	==================
	장바구니 상품 삭제
	==================
	*/
	$('.cart-del').on('click', function(){
		// 서버와 통신
		$.ajax({
			url:'deleteCart.do',
			type:'post',
			data:{cart_num:$(this).attr('data-cartnum')},
			dataType:'json',
			success:function(param){
				if(param.result == 'logout'){
					alert('로그인 후 사용 가능합니다.');
				}
				else if(param.result=='success'){
					alert('선택한 상품 삭제가 완료되었습니다.');					
					location.href='list.do';
				}
				else{
					alert('장바구니 상품을 삭제하는데 오류가 발생하였습니다.');
				}
			},
			error:function(){
				alert('네트워크 오류가 발생하였습니다.');
			}
		})
	})
	
	
	/*
	============================
	장바구니 상품 주문 수량 변경
	============================
	*/
	/* 변경 버튼의 부모(변경버튼, 수량 변경 창 포함)를 불러와서 수량에 접근하기*/
	$('.cart-modify').on('click',function(){
		/* 버튼에서 부모로 올라가서 input의 이름이 order_quantity인 값을 가져온다*/
		const input_quantity = $(this).parent().find('input[name="order_quantity"]')
		if(input_quantity.val() == ''){
			alert('수량을 입력하세요');
			input_quantity.focus();
			return;
		}
		/* val -> 바뀐 값*/
		if(input_quantity.val() < 1){
			alert('상품의 최소 수량은 1입니다.');	
			// 태그에 명시한 원래 value값을 읽어옴 attr(value) -> 원래 준 값
			input_quantity.val(input_quantity.attr('value'));
		}
		
		// 서버와 통신
		$.ajax({
			url:'modifyCart.do',
			type:'post',
			data:{cart_num:$(this).attr('data-cartnum'),item_num:$(this).attr('data-itemnum'), order_quantity:input_quantity.val()},
			dataType:'json',
			success:function(param){
				if(param.result == 'logout'){
					alert('로그인 후 사용해주세요.');
				} 
				else if(param.result == 'noSale'){
					alert('판매 중지된 상품입니다.');
				} 
				else if(param.result == 'overQuantity'){
					alert('상품의 재고가 부족합니다. 다시 입력해주세요.');
					input_quantity.val(input_quantity.attr('value'));
				} 
				else if (param.result == 'success'){
					alert('상품 수량이 변경되었습니다.');
					location.href='list.do';
				} else {
					alert('수량 변경 중 오류가 발생하였습니다.');
				}
			},
			error:function(){
				alert('네트워크 오류가 발생하였습니다.');
			}
		})
	})
	
	
});