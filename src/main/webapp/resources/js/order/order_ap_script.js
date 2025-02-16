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

});

//각 입력 필드 가져오기
document.querySelectorAll('.openPopupBtn').forEach(function(button){
	var ap_idx = button.getAttribute('data-index_2');
	var apc_order_cd = document.querySelector('.apc_order_cd[data-index_2="' + ap_idx + '"]');
	var approval_log = document.querySelector('.approval_log[data-index_2="' + ap_idx + '"]');
	var ap_process_st = "";
	
	//승인 버튼
	var approvalButton = document.querySelector('.button-1[data-index_2="' + ap_idx + '"]');
	if(approvalButton){
		approvalButton.addEventListener('click',function(){
			ap_process_st = "승인";
			updateAPValues();
			order_ap_frm.action = "/order/order_approval_change";
			order_ap_frm.method = "post";
			order_ap_frm.submit();	
		});
	}
	
	//거절 버튼
	var rejectButton = document.querySelector('.button-2[data-index_2="' + ap_idx + '"]');
	if(rejectButton){
		rejectButton.addEventListener('click',function(){
			ap_process_st = "거절";
			updateAPValues();
			order_ap_frm.action = "/order/order_approval_change";
			order_ap_frm.method = "post";
			order_ap_frm.submit();
		});
	}
	
	//히든 필드 업데이트 함수
	function updateAPValues(){
		document.getElementById('ap_order_idx').value = ap_idx;
		document.getElementById('ap_order_cd').value = apc_order_cd.value;
		document.getElementById('ap_process_st').value = ap_process_st;
		document.getElementById('ap_approval_log').value = approval_log.value;			
	}
	
});