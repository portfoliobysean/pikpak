document.addEventListener('DOMContentLoaded', function() {
    var openPopupBtn = document.getElementById('openPopupBtn');
    var closePopupBtn = document.getElementById('closePopupBtn');
    var closePopupBtn2 = document.getElementById('closePopupBtn2');
    var popupContainer = document.getElementById('popupContainer');
    var outgoing_cd_search = document.getElementById('outgoing_cd_search');
    var productQuantity = document.getElementById('productQuantity');
    var returnQuantity = document.getElementById('returnQuantity');
	var productPrice = document.getElementById('productPrice');
    var totalPrice = document.getElementById('totalPrice');
    var enrollBtn = document.getElementById('enrollBtn');
    var outgoingCode = document.getElementById('outgoingCode');

    // Open the popup
    openPopupBtn.addEventListener('click', function() {
        popupContainer.style.display = 'flex';
    });

    // Close the popup
    closePopupBtn.addEventListener('click', function() {
        popupContainer.style.display = 'none';
    });
    closePopupBtn2.addEventListener('click', function() {
        popupContainer.style.display = 'none';
    });

    // Optionally close the popup when clicking outside of it
    window.addEventListener('click', function(event) {
        if (event.target === popupContainer) {
            popupContainer.style.display = 'none';
        }
    });
    
    //출고 코드 조회
	outgoing_cd_search.addEventListener('click',function(){
		if(outgoingCode.value == ""){
			alert('출고 코드를 입력해 주세요.');
		}
		else{
			fetch('/return/outgoing_cd_searchck', {
	            method: 'POST',
	            headers: {
	                'Content-Type': 'application/x-www-form-urlencoded'
	            },
	            body: new URLSearchParams({ outgoing_cd: outgoingCode.value }),
	        })
	        .then(response => response.text())
	        .then(data => {
				if(data == "overlap"){
					outgoingCode.value="";
					alert("해당 출고 코드는 이미 반품 신청이 완료되었습니다.");
				}
	            else if(data == "no"){
					outgoingCode.value="";
					alert("해당 출고 코드는 미등록된 코드입니다.");
				}
				else{
					outgoingCode.readOnly = true;
					var outgoing_list = data.split(",");
					document.getElementById('product_cd').value = outgoing_list[0];
					document.getElementById('product_nm').value = outgoing_list[1];
					document.getElementById('productManufacturer').value = outgoing_list[2];
					document.getElementById('outgoingDate').value = outgoing_list[3];
					productQuantity.value = outgoing_list[4];
					productPrice.value = outgoing_list[5];
					document.getElementById('v_productPrice').value = parseInt(outgoing_list[5]).toLocaleString();
				}
	            
	        })
	        .catch((error) => {
	            console.log(error);
	        });
		}
	});
	
	//수량 체크
	function qtyck(){
		var execute = "yes";
		
		if(productPrice.value == ""){
			execute = "no";
			returnQuantity.value = "";
			alert('출고 코드를 먼저 조회해주세요.');
		}
			
		var qty = returnQuantity.value;
		var valid = /^[1-9]\d*$/.test(qty);
		
		if(!valid || qty.includes('.')){
			execute = "no";
			returnQuantity.value = "";
			if(qty != 0){
				alert('잘못된 값입니다.');				
			}
		}
		
		if(qty > parseInt(productQuantity.value)){
			returnQuantity.value = "";
			execute = "no";
			alert('반품 개수는 주문 개수보다 많을 수 없습니다.');
		}
		
		//총 가격
		if(execute == "yes"){
			totalPrice.value = qty*productPrice.value;
			document.getElementById('v_totalPrice').value = (qty*productPrice.value).toLocaleString();
		}
		else{
			totalPrice.value = "";
		}
	}
	
	returnQuantity.addEventListener('input',qtyck);
	
	//등록 버튼
	enrollBtn.addEventListener('click', function() {
        if(outgoingCode=="" || returnQuantity.value==""){
			alert('입력하지 않은 값이 존재합니다.');
		}
		else{
			return_frm.action = "/return/return_enroll";
			return_frm.method = "post";
			return_frm.submit();
		}
    });

});