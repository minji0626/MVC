<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 등록</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
<script type="text/javascript">
window.onload=function(){
	const myForm = document.getElementById('write_form');
	//이벤트 연결
	myForm.onsubmit=function(){
		const radio = document.querySelectorAll(
				              'input[type=radio]:checked');
		if(radio.length < 1){
			alert('상품표시여부를 지정하세요!');
			return false;
		}
		
		const items = document.querySelectorAll('.input-check');
		for(let i=0;i<items.length;i++){
			if(items[i].value.trim()==''){
				const label = document.querySelector(
						       'label[for="'+items[i].id+'"]');
				alert(label.textContent + ' 항목은 필수 입력');
				items[i].value = '';
				items[i].focus();
				return false;
			}
		}
	};
};
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>상품 등록</h2>
		<form id="write_form" action="adminWrite.do" method="post"
		                     enctype="multipart/form-data">
			<ul>
				<li>
					<label>상품표시여부</label>
					<input type="radio" name="status" value="1"
					                             id="status1">미표시
					<input type="radio" name="status" value="2"
					                             id="status2">표시                             
				</li>
				<li>
					<label for="name">상품명</label>
					<input type="text" name="name" id="name"
					                maxlength="10" class="input-check">
				</li>
				<li>
					<label for="price">가격</label>
					<input type="number" name="price" id="price"
					   min="1" max="999999999" class="input-check">
				</li>
				<li>
					<label for="quantity">수량</label>
					<input type="number" name="quantity" id="quantity"
					   min="0" max="9999999" class="input-check">
				</li>
				<li>
					<label for="photo1">상품사진1</label>
					<input type="file" name="photo1"
					  class="input-check"
					  id="photo1" accept="image/gif,image/png,image/jpeg">
				</li>
				<li>
					<label for="photo2">상품사진2</label>
					<input type="file" name="photo2"
					  class="input-check"
					  id="photo2" accept="image/gif,image/png,image/jpeg">
				</li>
				<li>
					<label for="detail">상품설명</label>
					<textarea rows="5" cols="30"
					  name="detail" id="detail"
					  class="input-check"></textarea>
				</li>
			</ul>   
			<div class="align-center">
				<input type="submit" value="등록">
				<input type="button" value="목록"
				              onclick="location.href='adminList.do'">
			</div>                     
		</form>
	</div>
</div>
</body>
</html>







