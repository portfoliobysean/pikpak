// 전역 변수
var selectedZone = null;
var selectedRack = null;
var selectedRackStatus = null;

// 페이지 로드 시 모든 선택 값을 초기화하고 기본값을 설정
document.addEventListener('DOMContentLoaded', function() {
    selectedZone = null;
    selectedRack = null;
    selectedRackStatus = null;
    updateRackInfo(); // 초기 값으로 정보 업데이트

    // 모든 `.rack` 요소에 대해 클릭 이벤트 추가 (한 번만 호출)
    document.querySelectorAll('.rack').forEach(function(rack) {
        rack.addEventListener('click', function() {
            inventory_specific_position(this); // 클릭한 랙을 함수로 전달하여 처리
        });
    });
});

// 알림 메시지 함수
function showAlertMessage() {
    alert('먼저 구역을 선택하세요!');
}

// 구역 선택 시 
function selectZone(zoneId) {
    selectedZone = zoneId; // 선택된 구역 저장
    updateRackInfo(); // 정보 업데이트

    const infoContainer = document.getElementById(`info-${zoneId}`);

    // 이미 열려 있는 구역을 클릭하면 닫기
    if (infoContainer.classList.contains('active')) {
        infoContainer.classList.remove('active');
        return;
    }

    // 모든 구역 정보 컨테이너를 숨김
    document.querySelectorAll('.zone-info-container').forEach(container => {
        container.classList.remove('active');
    });

    // ajax로 구역 정보 + 재고 정보 가져오기
    fetch('/inventory/getMultiAreaData', {
        method: 'POST',
        headers: { 'content-type': 'application/json' },
        body: JSON.stringify({
			keyType : 1,
			total_location: zoneId })
    })
    .then(response => response.json())
    .then(data => {
        const { getAreaData, getAreaStockData } = data;
        const { manager_nm, manager_tel, lastcheck_start_dt, lastcheck_end_dt, area_st, full_capacity, available_space } = getAreaData;
        
        const formattedStartDt = formatDateTime(lastcheck_start_dt);
        const formattedEndDt = formatDateTime(lastcheck_end_dt);
        
        // 구역 정보 업데이트
        infoContainer.innerHTML = `
            <div class="zone-info">
                <p><strong>담당자:</strong> ${manager_nm}</p>
                <p><strong>연락처:</strong> ${manager_tel}</p>
                <p><strong>마지막 점검일시:</strong><br> ${formattedStartDt}<br> ~ ${formattedEndDt}</p>
                <p><strong>상태:</strong> ${area_st}</p>
                <p><strong>가용 적재 용량:</strong> ${available_space}</p>
                <p><strong>구역 전체 적재용량:</strong> ${full_capacity}</p>
            </div>
        `;

        // 재고 리스트 업데이트
        updateStockList(getAreaStockData);

        // 선택된 구역의 정보 컨테이너를 활성화
        setTimeout(() => infoContainer.classList.add('active'), 100);

        // 선택된 구역에 대한 상태 업데이트
        document.querySelectorAll('.zone').forEach(function(area) {
            area.classList.remove('selected');
        });
        document.querySelector(`[data-id="${zoneId}"]`).classList.add('selected');
        document.getElementById('rack-info-text').textContent = `${selectedZone} 구역의 재고 리스트`;
    })
    .catch(error => console.log(error));
}

//시간 커스텀
function formatDateTime(dateTimeString){
	const dateTime = dateTimeString.split('T');
	return `${dateTime[0]} ${dateTime[1].substring(0,5)}`;
}


// 재고 리스트 업데이트 (중복 제거)
function updateStockList(stockData) {
    const tablebody = document.querySelector('.table2 tbody');
    tablebody.innerHTML = ''; // 기존 데이터 초기화

    if (stockData.length > 0) {
        stockData.forEach(item => {
            const row = `<tr>
                            <td>${item.product_cd}</td>
                            <td>${item.location_cd}</td>
                            <td>${item.product_nm}</td>
                            <td>${item.product_qty}</td>
                        </tr>`;
            tablebody.innerHTML += row;
        });
    } else {
        tablebody.innerHTML = '<tr><td colspan="4">등록된 재고가 없습니다.</td></tr>';
    }
}

// 랙 선택 시 
function selectRack(areaId) {
    if (!selectedZone) {
        showAlertMessage();
        return;
    }
    
    selectedRack = areaId;
    updateRackInfo(); // 정보 업데이트

    // 모든 랙 선택 해제
    document.querySelectorAll('.rack-number').forEach(function(area) {
        area.classList.remove('selected');
    });

    // ajax로 랙 정보 가져오기
    fetch('/inventory/getMultiAreaData', {
        method: 'POST',
        headers: { 'content-type': 'application/json' },
        body: JSON.stringify({ 
			keyType : 2,
			total_location: selectedZone + areaId })
    })
    .then(response => response.json())
    .then(data => {
        const { getAreaRackData } = data;
        updateStockList(getAreaRackData); // 재고 리스트 업데이트

        // 선택된 랙에 클래스 추가
        document.querySelector(`[data-id="${areaId}"]`).classList.add('selected');
        document.getElementById('rack-info-text').textContent = `${selectedZone} 구역 · ${selectedRack} 번 랙의 재고 리스트`;
    });
}

// 단, 열번호 선택 시
function inventory_specific_position(level_part) {
    if (!selectedZone) {
        showAlertMessage();
        return;
    } else if (!selectedRack) {
        alert('랙번호를 선택하세요.');
        return;
    }
	
    // 모든 랙 선택 해제
    document.querySelectorAll('.rack').forEach(r => r.classList.remove('selected'));

    // 선택된 랙에 `selected` 클래스 추가
    level_part.classList.add('selected');

    const level = level_part.closest('.level').getAttribute('data-level');
    const rackPosition = level_part.getAttribute('data-id');

    // 열번호 선택 처리
    selectRackStatus(level, rackPosition);
    
    fetch('/inventory/getMultiAreaData',{
        method: 'POST',
        headers: { 'content-type': 'application/json' },
        body : JSON.stringify({
			keyType : 3,
			total_location : selectedZone+selectedRack+level+rackPosition})		
	})
	.then(response => response.json())
	.then(data => {
		const { getTotalLocationData } = data;
		updateStockList(getTotalLocationData);
	})
}

// 열번호 선택 시 동작 처리
function selectRackStatus(level, rackPosition) {
    if (selectedZone && selectedRack) {
        selectedRackStatus = `${level} · ${rackPosition}`;
        updateRackInfo(); // 정보 업데이트
    }
}

// 구역, 랙 번호, 랙 상태가 모두 선택되었을 때 정보를 업데이트
function updateRackInfo() {
    const rackInfoText = document.getElementById('rack-info-text');

    if (selectedZone && selectedRack && selectedRackStatus) {
        rackInfoText.textContent = `${selectedZone} 구역 · ${selectedRack} 번 랙의 ${selectedRackStatus}의 재고 리스트`;
    } else {
        rackInfoText.textContent = '선택된 값이 없습니다.';
        updateStockList([]); // 빈 리스트로 업데이트
    }
}
