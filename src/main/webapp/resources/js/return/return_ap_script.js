document.addEventListener('DOMContentLoaded', function() {

	// 팝업 열기 버튼 클릭 시 팝업을 열기
	document.querySelectorAll('.openPopupBtn').forEach(function(button) {
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

	//각 입력 필드 가져오기
	document.querySelectorAll('.openPopupBtn').forEach(function(button) {
		var rt_idx = button.getAttribute('data-index');	//idx값
		var apc_return_cd = document.querySelector('.apc_return_cd[data-index="' + rt_idx + '"]');
		var reprocess_log = document.querySelector('.reprocess_log[data-index="' + rt_idx + '"]');
		var productTypeGroup = document.querySelector('.productTypeGroup[data-index="' + rt_idx + '"]');
		var product_type = document.querySelector('.product_type[data-index="' + rt_idx + '"]');
		var reprocess_wk = document.querySelector('.reprocess_wk[data-index="' + rt_idx + '"]');
		var ra_product_cd = document.querySelector('.ra_product_cd[data-index="' + rt_idx + '"]');
		var ra_user_id = document.querySelector('.ra_user_id[data-index="' + rt_idx + '"]');
		var ra_supplier = document.querySelector('.ra_supplier[data-index="' + rt_idx + '"]');
		var ra_restock = document.querySelector('.ra_restock[data-index="' + rt_idx + '"]');
		var ra_productQuantity = document.querySelector('.ra_productQuantity[data-index="' + rt_idx + '"]');
		var ap_reprocess_st = "";

		//승인 버튼
		var approvalButton = document.querySelector('.button-1[data-index="' + rt_idx + '"]');
		if (approvalButton) {
			approvalButton.addEventListener('click', function() {
				ap_reprocess_st = "승인";
				updateAPValues();
				return_ap_frm.action = "/return/return_approval_change";
				return_ap_frm.method = "post";
				return_ap_frm.submit();
			});
		}

		//거절 버튼
		var rejectButton = document.querySelector('.button-2[data-index="' + rt_idx + '"]');
		if (rejectButton) {
			rejectButton.addEventListener('click', function() {
				ap_reprocess_st = "거절";
				updateAPValues();
				return_ap_frm.action = "/return/return_approval_change";
				return_ap_frm.method = "post";
				return_ap_frm.submit();
			});
		}

		//완료 버튼
		var rejectButton = document.querySelector('.button-4[data-index="' + rt_idx + '"]');
		if (rejectButton) {
			rejectButton.addEventListener('click', function() {
				ap_reprocess_st = "완료";
				updateAPValues();
				if(reprocess_wk.value=="재입고"){
					if(ra_restock.value==""){
						alert("재입고 수를 입력해 주세요.");
					}
					else if(ra_restock.value!=""){
						return_ap_frm.action = "/return/return_approval_change";
						return_ap_frm.method = "post";
						return_ap_frm.submit();
					}
				}
				else if(reprocess_wk.value=="폐기"){
					return_ap_frm.action = "/return/return_approval_change";
					return_ap_frm.method = "post";
					return_ap_frm.submit();
				}
			});
		}

		//히든 필드 업데이트 함수
		function updateAPValues() {
			document.getElementById('ap_return_cd').value = apc_return_cd.value;
			document.getElementById('ap_reprocess_st').value = ap_reprocess_st;
			if (ap_reprocess_st == "승인" || ap_reprocess_st == "거절") {
				document.getElementById('ap_reprocess_log').value = reprocess_log.value;
				document.getElementById('ap_reprocess_wk').value = "대기";
			}
			else {
				document.getElementById('exreceiving_size').value = product_type.value;
				document.getElementById('ap_reprocess_wk').value = reprocess_wk.value;
				document.getElementById('ra_product_cd').value = ra_product_cd.value;
				document.getElementById('ra_user_id').value = ra_user_id.value;
				document.getElementById('ra_supplier').value = ra_supplier.value;
				document.getElementById('ra_restock').value = ra_restock.value;
			}
		}

		//재입고 / 폐기
		//초기 상태 확인
		if (reprocess_wk != null) {
			updateProductTypeGroup(reprocess_wk.value);

			//선택 값 변화에 따른 동작
			reprocess_wk.addEventListener('change', function() {
				updateProductTypeGroup(this.value);
			});

			//상태 업데이트 함수
			function updateProductTypeGroup(value) {
				if (value === '재입고') {
					productTypeGroup.style.display = 'block';
				} else if (value === '폐기') {
					productTypeGroup.style.display = 'none';
				}
			}

			//재입고시 값 조정
			ra_restock.addEventListener('input',restock_ck);
			
			function restock_ck(){
				var valid = /^[1-9]\d*$/.test(ra_restock.value);
				
				if(!valid){
					ra_restock.value="";
					alert('재입고 수에는 숫자만 입력할 수 있습니다.');
				}
				
				if(ra_restock.value > parseInt(ra_productQuantity.value)){
					ra_restock.value="";
					alert('재입고 수는 반품 개수보다 많을 수 없습니다.');
				}
			}
			
		}
	});
});