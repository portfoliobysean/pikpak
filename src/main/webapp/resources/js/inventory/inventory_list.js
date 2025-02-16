function toggleCheckbox(row) {
    // 클릭한 행에서 첫 번째 체크박스를 찾음
    var checkbox = row.querySelector('input[type="checkbox"]');
    
    // 체크박스가 존재하면, 체크 상태를 토글
    if (checkbox) {
        checkbox.checked = !checkbox.checked;
    }
}
let activeUser= "";
window.onload = function() {
    getSessionInfo();
};

function getSessionInfo() {
    fetch('/inventory/getSessionInfo')
        .then(response => response.json())
        .then(data => {
            console.log("Session info: ", data.activeUserID);
            // 세션 정보를 필요한 곳에 사용
            document.getElementById('operator_id').value = data.activeUserID;
        })
        .catch(error => {
            console.error('Error fetching session info:', error);
        });
}


//조회 버튼 클릭 (form전송)
document.getElementById('searchButton').addEventListener('click', function(event){
	event.preventDefault();
	//폼데이터 가져오기
	const form = document.getElementById('searchForm');
	const formData = new FormData(form);
	const searchData = {};
	let hasInput = false;
	
	//입력되지않은값 제외하고 searchParams에 추가
	formData.forEach((value, key) => {
		if(value){
			searchData[key] = value;
			hasInput = true;
		}
	});
	
	if (!hasInput) {
		alert('검색할 항목을 입력하세요.');
		return false;  // 폼 전송 중단
	}
		
	fetch('/inventory/getSearchData',{
		method : 'POST',
		headers : {
			'content-type' : 'application/json'
		},
		body : JSON.stringify(searchData)
	})
	.then(response => response.json())
	.then(data => {
		console.log(data);
		renderSearchResults(data);
		if(data){
			alert('조회되었습니다.');
		}else{
			alert('조회된 정보가 없습니다.');
			location.reload();
		}
	})
	.catch(error=> console.error('Error fetching inventory data:',error));
})


function renderSearchResults(data){
	const resultsContainer = document.getElementById('results');
	resultsContainer.innerHTML = '';
	
	if(data.length ==0){
		const noResults = document.createElement('tr');
		noResults.innerHTML = `<td colspan="7"> 검색 결과가 없습니다.</td>`;
		resultsContainer.appendChild(noResults);
		return;
	}
	
	data.forEach(item => {
		console.log(data);
		const row = document.createElement('tr');
		row.innerHTML=`
            <td><input type="checkbox" onclick="event.stopPropagation();"></td>
            <td>${item.location_cd}</td>
            <td>${item.product_cd}</td>
            <td>${item.company_name}</td>  <!-- company_name 출력 -->
            <td>${item.product_nm}</td>
            <td>${item.safetyinventory_qty}</td>  <!-- safetyinventory_qty 출력 -->
            <td>${item.product_qty}</td>
            <td style="text-align: center;">${item.product_type}</td> <!-- product_type 출력 -->
            <td style="text-align: center;">${item.product_price}</td> <!-- product_price 출력 -->
            <td style="text-align: center;">
                <button class="btn btn-info" onclick="event.stopPropagation(); openModal(${item.wh_warehouse_idx})" data-toggle="modal" data-target="#inventoryModal">관리</button>
            </td>
		`;
		resultsContainer.appendChild(row);
	})
}


//관리 버튼 클릭 시 모달창 열림
function openModal(wh_warehouse_idx){
	document.getElementById('wh_warehouse_idx').value = wh_warehouse_idx;
	const k = 1;
	fetch(`/inventory/getInventoryDetails?wh_warehouse_idx=${wh_warehouse_idx}&k=${k}`,{
		method : 'GET',
	})
	.then(response => response.json())
	.then(data => {
		const details = data.details;
		let safety_qty = "";
		if(data.safety_inventory == null){
			safety_qty = "안전 재고 없음";
		}else{
			safety_qty = data.safety_inventory;
		}
		
		document.getElementById('productCode').value= details.product_cd;
		document.getElementById('productName').value= details.product_nm;
		document.getElementById('locationCode').value= details.location_cd;
		document.getElementById('currentStock').value= details.product_qty;	//현재 재고
		
		document.getElementById('incomingStock').value= data.incoming_stock;	//입고 테이블에서 가져와야함
		document.getElementById('outgoingStock').value= data.outgoing_stock;
		
		document.getElementById('safetyStock').value= safety_qty;	//안전 재고
		
		document.getElementById('notes').value = details.inventory_log;
		
	$('#inventoryModal').modal('show');		
		
	})
	.catch(error => console.error('Error fetching inventory data:',error));
}


// 폐기 버튼 클릭 이벤트 처리
document.getElementById('IvDelete').addEventListener('click', function() {
    const wh_warehouse_idx = document.getElementById('wh_warehouse_idx').value;
    const disposeReason = document.getElementById('disposeReason').value;
    const disposeQuantity = document.getElementById('disposeQuantity').value;
    const currentStock = document.getElementById('currentStock').value;
    const locationCode = document.getElementById('locationCode').value;
    const productName = document.getElementById('productName').value;    
    
    if (!disposeReason) {
        alert('폐기 사유를 입력하세요.');
        return;
    }

    if (!disposeQuantity || disposeQuantity <= 0) {
        alert('폐기할 수량을 입력하세요.');
        return;
    }

    if (parseInt(disposeQuantity) > parseInt(currentStock)) {
        alert('폐기 수량이 현재 재고량보다 많을 수 없습니다.');
        return;
    }
    // confirm 창에서 정보 제공 및 사용자 확인
    const confirmDispose = confirm(`위치코드 '${locationCode}'의 '${productName}'을(를) ${disposeQuantity}개 폐기처리하시겠습니까?`);
    
    if (!confirmDispose) {
        // 사용자가 '취소'를 눌렀을 때 아무 작업도 하지 않음
        return;
    }
    
    
    
    // 폐기 처리 요청
    fetch('/inventory/deleteWarehouse', {
        method: 'POST', // POST 메소드로 변경 (DELETE는 데이터 전송에 제한이 있을 수 있음)
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            wh_warehouse_idx: wh_warehouse_idx,
            disposeReason: disposeReason,
            disposeQuantity: disposeQuantity,
            currentStock: currentStock
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            if (data.action === 'delete') {
                alert('전체 재고 폐기 처리가 완료되었습니다.');
            } else if (data.action === 'update') {
                alert('일부 재고 폐기 처리가 완료되었습니다.');
            }
            location.reload();  // 페이지 새로고침
        } else {
            alert(data.message || '폐기 처리에 실패하였습니다.');
        }
    })
    .catch(error => console.error('Error:', error));
});

document.getElementById('IvModify').addEventListener('click', function() {
	
       const wh_warehouse_idx = document.getElementById('wh_warehouse_idx').value;
       const notes = document.getElementById('notes').value;
        
       if (!notes.trim()) {
           alert("비고란 내용을 입력하세요.");
           return;
       }
       
       const confirmModify = confirm("정말로 비고란을 수정하시겠습니까?");
	    if (!confirmModify) {
	        // 사용자가 '취소'를 눌렀을 때 아무 작업도 하지 않음
	        return;
	    }       
       // AJAX 요청으로 비고란 업데이트 처리
       fetch('/inventory/updateWarehouseNotes', {
           method: 'POST',
           headers: {
               'Content-Type': 'application/json'
           },
           body: JSON.stringify({
               wh_warehouse_idx: wh_warehouse_idx,
               notes: notes
           })
       })
       .then(response => response.json())
       .then(data => {
           if (data.success) {
               alert('비고란이 성공적으로 업데이트되었습니다.');
               location.reload();
           } else {
               alert('비고란 업데이트에 실패했습니다.');
           }
       })
       .catch(error => {
           console.error('Error:', error);
       });	
});


//검색 옵션 -> 상품 드롭다운 데이터
function fetchProductData(value, keyType) {
    if (!value) {
        return; // 값이 없으면 아무 작업도 하지 않음
    }

    fetch('/inventory/getProductInfo', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            keyType: keyType, // 1: 상품코드 조회, 2: 상품명 조회
            data: value
        }),
    })
    .then(response => response.json())
    .then(data => {
		const productData = data.data;
        if (keyType === 1) {
            // 상품코드 선택 시 상품명을 업데이트
            document.getElementById('product_nm').value = productData.product_nm;
        } else if (keyType === 2) {
            // 상품명 선택 시 상품코드를 업데이트
            document.getElementById('product_cd').value = productData.product_cd;
        }
    })
    .catch(error => console.error('Error fetching product data:', error));
}


//검색 옵션 -> 회사 드롭다운 데이터 
function fetchCompanyData(value, keyType) {
    if (!value) {
        return;
    }

    fetch('/inventory/getSupplierInfo', {
        method: 'post',
        headers: { 'content-type': 'application/json' },
        body: JSON.stringify({
            keyType: keyType,
            data: value
        })
    })
    .then(response => response.json())
    .then(data => {
        if (keyType === 1) {
            // 회사명 선택 시 회사코드 업데이트
            document.getElementById('supplier_cd').value = data.supplier_cd;
        } else if (keyType === 2) {
            // 회사코드 선택 시 회사명 업데이트
            document.getElementById('supplier_nm').value = data.supplier_nm;
        }
    })
    .catch(error => console.error('Error fetching company data:', error));
}