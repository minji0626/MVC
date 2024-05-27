$(function(){
	/*
	=============
	주문 수량 변경
	==============
	*/
	
	$('#order_quantity').on('keyup mouseup',function(){
		if($('#order_quantity').val()==''){
			$('#item_total_txt').text('총 주문 금액 : 0원');
			return;
		}
		if($('#order_quantity').val()<=0){
			$('#order_quantity').val('');
			return;
		}
		if(Number($('#item_quantity').val()) < $('#order_quantity').val()){
			alert('수량이 부족합니다');
			$('#order_quantity').val('');
			$('#item_total_txt').text('총 주문 금액 : 0원');
			return;
		}
		
		let total = $('#item_price').val() * $('#order_quantity').val();
		
		$('#item_total_txt').text('총 주문 금액 : '+total.toLocaleString()+'원');
	});
})