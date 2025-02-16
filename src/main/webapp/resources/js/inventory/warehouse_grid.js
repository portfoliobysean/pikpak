var inventoryData = {};	//각 구역의 데이터를 저장
$(document).ready(function () {
	// 페이지 최초 출력시는 A구역으로 출력
	loadZoneData('A');
	
	//모달창 출력
	$('#inspectionModal').modal('hide');
	
	//위치 지정 버튼 비활성화 (위치 조회 시에만 활성화)
	$('#assign-location-btn').prop('disabled', true);
	
    // 회사 선택 시 hidden 필드 업데이트
    $('#company-select').on('change', function() {
        updateHiddenField(); // 선택된 회사 이름을 hidden 필드에 설정
    });	
	
    // 위치 조회 버튼 클릭 이벤트
    $('#check-location-btn').on('click', function() {
        // 선택된 구역, 랙, 레벨, 파트 정보 가져오기
        const zone = $('#zone-select').val();
        const rack = $('#rack-select').val();
        const level = $('#level-select').val();
        const part = $('#part-select').val();
        
        const locationCode = `${zone}${rack}${level}${part}`;

        // 서버에 조회 요청 보내기 (AJAX)
        $.ajax({
            url: '/inventory/checkLocation',
            method: 'POST',
            data: JSON.stringify({ location_cd: locationCode }),
            contentType: 'application/json',
            success: function(response) {
                if (response.status === 'occupied') {
                    // 이미 사용 중인 위치일 때
                    alert('이미 사용 중인 위치입니다. 위치를 수정하거나 다른 위치를 선택하세요.');
                    // 재고가 있는 경우 수정만 가능
					 $('#assign-location-btn').prop('disabled', true);
                } else {
                    // 사용 가능한 위치일 때
                    alert('이 위치는 사용 가능합니다.');
                    $('#assign-location-btn').prop('disabled', false);
                    $('#assign-location-btn').text('위치 지정');
                }
            },
            error: function() {
                alert('위치 조회 중 오류가 발생했습니다.');
            }
        });
    });

    // 위치 지정 버튼 클릭 이벤트
    $('#assign-location-btn').on('click', function() {
        const zone = $('#zone-select').val();
        const rack = $('#rack-select').val();
        const level = $('#level-select').val();
        const part = $('#part-select').val();
        const companyId = $('#company-select').val();
        const companyName = document.getElementById('company-select').options[document.getElementById('company-select').selectedIndex].text;

        const locationCode = `${zone}${rack}${level}${part}`;
		const confirmationMessage = `${companyName} 의 위치를 ${locationCode} 로 지정하시겠습니까?`;
		if(confirm(confirmationMessage)){
	        // 위치 등록 요청 (AJAX)
	        $.ajax({
	            url: '/inventory/assignLocation',
	            method: 'POST',
	            data: JSON.stringify({
	                location_cd: locationCode,
	                supplier_cd: companyId
	            }),
	            contentType: 'application/json',
	            success: function(response) {
	                alert(response.message);
	                $('#locationModal').modal('hide'); // 모달 닫기
	                location.reload();
	            },
	            error: function() {
	                alert('위치 지정 중 오류가 발생했습니다.');
	            }
	        });
		}else{
			alert('위치 지정이 취소되었습니다.');
		}
    });	
});

//위치 지정 Confirm 메세지 출력
function updateHiddenField() {
    // 선택된 회사 이름을 가져옴
    const companySelect = document.getElementById('company-select');
    const selectedOption = companySelect.options[companySelect.selectedIndex].text;
    
    // hidden 필드에 선택된 회사 이름을 설정
    document.getElementById('company_nm_a').value = selectedOption;
}

// content 색 초기화
function resetRackColors(){
	const allRackParts = document.querySelectorAll('.wms-part');
	allRackParts.forEach(part => {
		part.classList.remove('no-stock', 'assigned-no-stock', 'no-space', 'partial-space');
        // 기존 popover가 존재하는지 확인 후 제거
	});
}

//탭으로 선택한 구역의 재고 데이터를 저장시킴
function loadZoneData(zoneId){
    // 모든 구역 버튼에서 active 클래스를 제거하고, 선택한 구역 버튼에만 추가
    document.querySelectorAll('.btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.getElementById(`zone-${zoneId.toLowerCase()}-tab`).classList.add('active');	

    // 이전 구역의 CSS 상태 초기화
    resetRackColors();	
	
	//데이터 로드
	fetch('/inventory/getZoneData',{
		method : 'post',
		headers :{
			'content-type' : 'application/json'},
			body : JSON.stringify({"area_cd" : zoneId})
	})
	.then(response => response.json())
	.then(data => {
		inventoryData[zoneId] = data.getAreaStockData;	//구역 전체 데이터를 저장
		//가져온 데이터를 기반으로 각 위치에 맞는 정보 설정
		updateRackInfo(zoneId,inventoryData[zoneId]);
		
		//재고 데이터를 기반으로 색상 업데이트
		updateRackColors(zoneId);
	}).catch(error => {
		console.log(error);
	});
}

// location_cd를 Rxx-Lx-Px 형식으로 변환하는 함수
function convertLocationFormatForQuery(locationCd) {
    const rackNumber = locationCd.slice(1, 3); // 랙 번호 (A01 -> 01)
    const rackLevel = locationCd.slice(4, 5);  // 단 (L1 -> L1)
    const rackPart = locationCd.slice(6);      // 열 (P1 -> P1)

    return `R${rackNumber}-L${rackLevel}-P${rackPart}`; // Rxx-Lx-Px 형식 반환
}   

//팝오버 내용 적용
function updateRackInfo(zoneId, zoneData) {    	
    // 위치별로 상품을 그룹화하기 위한 객체 생성
    const locationItemsMap = {};
    	
    //	zoneData에서 각 위치에 있는 모든 상품을 그룹화
     zoneData.forEach(item => {
        const locationKey = item.location_cd;
        if (!locationItemsMap[locationKey]) {
            locationItemsMap[locationKey] = [];
        }
        locationItemsMap[locationKey].push(item);
    });   
	// HTML 내 모든 data-location 요소에 대해 반복문 수행
	document.querySelectorAll('[data-location]').forEach(locationElement => {
	    const formattedLc = locationElement.getAttribute('data-location');
	    const lc = zoneId+formattedLc.slice(1,3)+formattedLc.slice(4,6)+formattedLc.slice(7,9);
	    // locationItemsMap에서 해당 위치에 대한 데이터를 가져옴
	    const locationData = locationItemsMap[lc];
	    let popoverContent="";
	    let assignedCompany =null;
	
	    if (locationData && locationData.length > 0) {
			assignedCompany = locationData[0].assigned_supplier_nm;
			
            // 할당된 회사 정보 먼저 표시
            if (assignedCompany) {
                popoverContent += `
                    <strong>위치 지정 회사:</strong> ${assignedCompany}<br><hr>
                `;
            }
            			
			locationData.forEach(item => {
                const hasStock = item.product_qty > 0;
                assignedCompany = item.assigned_supplier_nm;

                popoverContent += `
		            <div style="margin-bottom: 10px;">
		                <strong>상품명:</strong> ${item.product_nm || '재고 없음'}<br>
		                <strong>수량:</strong> ${item.product_qty || '재고 없음'}<br>
		                <strong>제조사:</strong> ${item.product_supplier_nm || '재고 없음'}
		            </div>
		        `;

                // 재고가 없고 할당된 회사가 있는 경우 삭제 버튼 추가
                if (!hasStock && assignedCompany) {
                    popoverContent += `
                        <button class="btn btn-danger btn-sm delete-location-btn" data-location="${lc}" style="display: block; margin-top: 10px;">
                            위치 삭제
                        </button>
                    `;
                }
                popoverContent += '<hr>'; // 각 항목 사이에 구분선 추가
            });

            // 마지막 구분선(<hr>) 제거
            popoverContent = popoverContent.slice(0, -4);			
	    } else {
	        // 데이터가 없을 경우 '데이터 없음' 메시지 설정
	        popoverContent = '<strong>해당 위치에 데이터가 없습니다.</strong>';
	    }
		    //popover 에 사용할 데이터 설정
            locationElement.setAttribute('data-content',popoverContent);
            // 새 popover 설정
            $(locationElement).popover({
                html: true,
                trigger: 'focus',
                placement: 'right',
                container: 'body'
            });                    
	});
}

//위치 삭제 버튼
document.addEventListener('click', function(event) {
    // 클릭한 요소가 'delete-location-btn' 클래스가 있는지 확인
    if (event.target.classList.contains('delete-location-btn')) {
        const locationCd = event.target.getAttribute('data-location');
        if (confirm('정말 이 위치를 삭제하시겠습니까?')) {
            // AJAX 요청 보내기 (fetch 사용)
            fetch('/inventory/deleteLocation', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ location_cd: locationCd })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success==true) {
                    alert('위치가 삭제되었습니다.');
                    window.location.reload(); // 페이지 새로고침
                } else {
                    alert('위치 삭제 중 오류가 발생했습니다.');
                }
            })
            .catch(error => {
                alert('위치 삭제 중 오류가 발생했습니다.');
                console.error('Error:', error);
            });
        }
    }
});


function convertLocationFormat(locationKey) {
    const rackNumber = locationKey.slice(1, 3); // 랙 번호 (01, 02, 03 등)
    const rackLevel = locationKey.slice(4, 5); // 단 (L1, L2, L3, L4)
    const partNumber = locationKey.slice(6); // 열 (P1, P2, P3 등)

    // 새로운 형식으로 변환
    const formattedLocation = `R${rackNumber}-L${rackLevel}-P${partNumber}`;
    
    return formattedLocation;
}

function updateRackColors(zoneId){
	//inventoryData에 저장된 해당 구역 데이터를 가져옴
	const zoneData = inventoryData[zoneId];
    	
    //모든 랙 위치 요소 초기화
     document.querySelectorAll('.wms-part').forEach(part => {
	   const partLocation = part.getAttribute('data-location');
	   const formattedLocation = partLocation.replace(/R(\d+)-L(\d+)-P(\d+)/, `${zoneId}$1L$2P$3`); // zoneId를 동적으로 삽입
	   
	   const locationData = zoneData.find(item => item.location_cd === formattedLocation);
        if (locationData) {
            const maxCapacity = locationData.max_capacity;
            const currentCapacity = locationData.current_capacity;
            // 색상 설정 로직
            if (currentCapacity === 0) {
                // 민트색 (할당된 위치, 재고 없음)
                part.classList.add('assigned-no-stock');
            } else if (currentCapacity == maxCapacity) {
                // 빨간색 (공간 다 찬 상태)
                part.classList.add('no-space');
            } else {
                // 파란색 (일부 공간 남음)
                part.classList.add('partial-space');
            }
        } else {
            // 해당 위치에 상품 데이터가 없을 경우 초록색 (할당되지 않은 위치)
            part.classList.add('no-stock');
        }	   
    });   
}

//특정 구역, 단, 열에 해당하는 데이터를 찾는 함수
function findPartData(zoneId,rackNumber,level,part){
	if(!inventoryData[zoneId]){
		return null;	//해당 구역에 데이터 없을 경우 
	}
    // rackNumber와 level을 각각 두 자리로 맞추기 위해 padStart 사용
    const formattedRackNumber = String(rackNumber).padStart(2, '0'); // 2자리로 맞추기
    const formattedLevel = String(level).padStart(1, '0'); // 1자리로 맞추기	
    
	//location_cd 생성
	const locationCode = `${zoneId}${formattedRackNumber}L${formattedLevel}P${part}`;
	//location_cd 가 정확히 일치하는 데이터를 찾음
	return inventoryData[zoneId].find(item => item.location_cd == locationCode);
}