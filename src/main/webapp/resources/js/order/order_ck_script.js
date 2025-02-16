document.addEventListener('DOMContentLoaded', function() {

	// 팝업 열기 버튼 클릭 시 팝업을 열기
	document.querySelectorAll('.openPopupBtn2').forEach(function(button) {
		button.addEventListener('click', function() {

			// 현재 버튼에 해당하는 팝업을 찾기
			var popupContainer = button.nextElementSibling;
			popupContainer.style.display = 'flex';

		});
	});

	// 팝업 닫기 버튼 클릭 시 팝업을 닫는 함수
	document.querySelectorAll('.close-btn').forEach(function(button) {
		button.addEventListener('click', function() {
			var popupContainer = button.closest('.popup-container');
			popupContainer.style.display = 'none';
		});
	});

	// 팝업 외부 클릭 시 팝업 닫기
	window.addEventListener('click', function(event) {
		if (event.target.classList.contains('popup-container')) {
			event.target.style.display = 'none';
		}
	});

	// 취소 버튼
	document.querySelectorAll('.button-3').forEach(function(button) {
		button.addEventListener('click', function() {
			var popupContainer = button.closest('.popup-container');
			popupContainer.style.display = 'none';
		});
	});

// 각 상품코드 입력 필드를 가져오기
document.querySelectorAll('.productCode').forEach(function(inputField) {
	//클래스 변수
	var class_idx = inputField.getAttribute('data-index');

	var productCode = document.querySelector('.productCode[data-index="' + class_idx + '"]');
	var productName = document.querySelector('.productName[data-index="' + class_idx + '"]');
	var productPrice = document.querySelector('.productPrice[data-index="' + class_idx + '"]');
	var productQuantity = document.querySelector('.productQuantity[data-index="' + class_idx + '"]');
	var totalPrice = document.querySelector('.totalPrice[data-index="' + class_idx + '"]');
	var v_totalPrice = document.querySelector('.v_totalPrice[data-index="' + class_idx + '"]');
	var startDate = document.querySelector('.startDate[data-index="' + class_idx + '"]');
	var endDate = document.querySelector('.endDate[data-index="' + class_idx + '"]');
	
	//수량 체크
	function qtyck() {
		if (productPrice.value == "") {
			productQuantity.value = "";
			alert('상품코드를 먼저 조회해주세요.');
		}

		var qty = productQuantity.value;
		var valid = /^[1-9]\d*$/.test(qty);

		if (!valid || qty.includes('.')) {
			productQuantity.value = "";
			if (qty != 0) {
				alert('잘못된 값입니다.');
			}
		}

		//총 가격
		totalPrice.value = qty * productPrice.value;
		v_totalPrice.value = (qty * productPrice.value).toLocaleString();
	}

	productQuantity.addEventListener('input', qtyck);

	//상품코드 조회
	var searchButton = document.querySelector('.button-2[data-index="' + class_idx + '"]');
	if (searchButton) {
		searchButton.addEventListener('click', function() {
			if (productCode.value == "") {
				alert('상품 코드를 입력해 주세요.');
			}
			else {
				fetch('/order/product_cd_searchck', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded'
					},
					body: new URLSearchParams({ product_cd: productCode.value }),
				})
					.then(response => response.text())
					.then(data => {
						if (data == "no") {
							productCode.value = "";
							alert("해당 상품 코드는 미등록된 코드입니다.");
						}
						else {
							productCode.readOnly = true;
							var product_list = data.split(",");
							document.querySelector('.productName[data-index="' + class_idx + '"]').value = product_list[0];
							document.querySelector('.productManufacturer[data-index="' + class_idx + '"]').value = product_list[1];
							document.querySelector('.productSize[data-index="' + class_idx + '"]').value = product_list[2];
							document.querySelector('.productWeight[data-index="' + class_idx + '"]').value = product_list[3];
							document.querySelector('.productPrice[data-index="' + class_idx + '"]').value = product_list[4];
						}

					})
					.catch((error) => {
						console.log(error);
					});
			}
		});
	}

	//수정 버튼
	var modifyButton = document.querySelector('.button-1[data-index="' + class_idx + '"]');
	if (modifyButton) {
		modifyButton.addEventListener('click', function() {
			if (productCode.value == "" || productQuantity.value == "" || startDate.value == "" || endDate.value == "") {
				alert('입력하지 않은 값이 존재합니다.');
			}
			else if (startDate.value > endDate.value) {
				alert('착수일과 납기일을 확인해 주세요.');
			}
			else {
				updateCKValues();
				order_ck_frm.action = "/order/order_modify";
				order_ck_frm.method = "post";
				order_ck_frm.submit();
			}
		});
	}
	
	//삭제 버튼
	var deleteButton = document.querySelector('.button-4[data-index="' + class_idx + '"]');
	if (deleteButton) {
		deleteButton.addEventListener('click', function() {
			if(confirm("주문 취소 후에는 복구가 불가능합니다. 주문 취소를 진행하시겠습니까?")){
				updateCKValues();
				order_ck_frm.action = "/order/order_delete";
				order_ck_frm.method = "post";
				order_ck_frm.submit();
			}
		});
	}

	//히든 필드 업데이트 함수
	function updateCKValues() {
		document.getElementById('ck_order_idx').value = class_idx;
		document.getElementById('ck_product_cd').value = productCode.value;
		document.getElementById('ck_product_nm').value = productName.value;
		document.getElementById('ck_order_qty').value = productQuantity.value;
		document.getElementById('ck_order_price').value = totalPrice.value;
		document.getElementById('ck_start_dt').value = startDate.value;
		document.getElementById('ck_due_dt').value = endDate.value;
	}

});


});