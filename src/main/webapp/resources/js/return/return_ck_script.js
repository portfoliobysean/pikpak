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
	//클래스 변수
	var class_idx = button.getAttribute('data-index');	
	
	//삭제 버튼
	var deleteButton = document.querySelector('.button-1[data-index="' + class_idx + '"]');
	if (deleteButton) {
		deleteButton.addEventListener('click', function() {
			if(confirm("반품 취소 후에는 복구가 불가능합니다. 반품 취소를 진행하시겠습니까?")){
				document.getElementById('ck_return_idx').value = class_idx;
				return_ck_frm.action = "/return/return_delete";
				return_ck_frm.method = "post";
				return_ck_frm.submit();
			}
		});
	}
	
});