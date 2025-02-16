document.addEventListener('DOMContentLoaded', function() {
	var today = new Date();
    var formattoday = today.toISOString().split('T')[0];
	
    var openPopupBtn = document.getElementById('openPopupBtn');
    var closePopupBtn = document.getElementById('closePopupBtn');
    var closePopupBtn2 = document.getElementById('closePopupBtn2');
    var popupContainer = document.getElementById('popupContainer');
    var product_cd_search = document.getElementById('product_cd_search');
	var productQuantity = document.getElementById('productQuantity');
	var productPrice = document.getElementById('productPrice');
    var totalPrice = document.getElementById('totalPrice');
    var v_totalPrice = document.getElementById('v_totalPrice');
    var enrollBtn = document.getElementById('enrollBtn');
    var startDate = document.getElementById('startDate');
    var endDate = document.getElementById('endDate');
    var productCode = document.getElementById('productCode');

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
    
    //착수일
    startDate.value = formattoday;
    
    function startDateck(){
		if(startDate.value < formattoday){
			startDate.value = formattoday;
			alert('착수일은 오늘 날짜('+ formattoday +')보다 이전일 수 없습니다.');
		}
	}
	
	startDate.addEventListener('input',startDateck);
    
    
	//상품코드 조회
	product_cd_search.addEventListener('click',function(){
		if(productCode.value == ""){
			alert('상품 코드를 입력해 주세요.');
		}
		else{
			fetch('/order/product_cd_searchck', {
	            method: 'POST',
	            headers: {
	                'Content-Type': 'application/x-www-form-urlencoded'
	            },
	            body: new URLSearchParams({ product_cd: productCode.value }),
	        })
	        .then(response => response.text())
	        .then(data => {
	            if(data == "no"){
					productCode.value="";
					alert("해당 상품 코드는 미등록된 코드입니다.");
				}
				else{
					productCode.readOnly = true;
					var product_list = data.split(",");
					document.getElementById("productName").value = product_list[0];
					document.getElementById("productManufacturer").value = product_list[1];
					document.getElementById("productSize").value = product_list[2];
					document.getElementById("productWeight").value = product_list[3];
					document.getElementById("productPrice").value = product_list[4];
					document.getElementById("v_productPrice").value = parseInt(product_list[4]).toLocaleString();
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
			productQuantity.value = "";
			alert('상품코드를 먼저 조회해주세요.');
		}
			
		var qty = productQuantity.value;
		var valid = /^[1-9]\d*$/.test(qty);
		
		if(!valid || qty.includes('.')){
			execute = "no";
			productQuantity.value = "";
			if(qty != 0){
				alert('잘못된 값입니다.');				
			}
		}
		
		//총 가격
		if(execute == "yes"){
			totalPrice.value = qty*productPrice.value;
			v_totalPrice.value= (qty*productPrice.value).toLocaleString();
		}
		else{
			totalPrice.value = "";
		}
	}	
	
	productQuantity.addEventListener('input',qtyck);
	
	//등록 버튼
	enrollBtn.addEventListener('click', function() {
        if(productCode.value=="" || productQuantity.value=="" || startDate.value=="" || endDate.value==""){
			alert('입력하지 않은 값이 존재합니다.');
		}
		else if(startDate.value>endDate.value){
			alert('착수일과 납기일을 확인해 주세요.');
		}
		else{
			order_frm.action = "/order/order_enroll";
			order_frm.method = "post";
			order_frm.submit();
		}
    });    
    
});