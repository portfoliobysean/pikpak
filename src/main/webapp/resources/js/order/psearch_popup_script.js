document.addEventListener("DOMContentLoaded", function() {
    const searchBtn = document.getElementById("ps_searchBtn");
    const popup = document.getElementById("ps_popup");
    const closeBtn = document.getElementById("ps_closeBtn");
    const closeBtn2 = document.getElementById("ps_closeBtn2");
    const ps_enterBtn = document.getElementById("ps_enterBtn");
    const ps_resetBtn = document.getElementById("ps_resetBtn");

    // 검색 버튼 클릭 시 팝업 열기
    searchBtn.addEventListener("click", function() {
        popup.style.display = "flex"; // Flex로 설정하여 팝업 표시
    });

    // 닫기 버튼 클릭 시 팝업 닫기
    closeBtn.addEventListener("click", function() {
        popup.style.display = "none"; // 팝업 숨기기
    });

    // 취소 버튼 클릭 시 팝업 닫기
    closeBtn2.addEventListener("click", function() {
        popup.style.display = "none"; // 팝업 숨기기
    });

    // 팝업 밖을 클릭 시 팝업 닫기
    popup.addEventListener("click", function(event) {
        if (event.target === popup) {
            popup.style.display = "none"; // 팝업 숨기기
        }
    });
    
    //검색 버튼
    ps_enterBtn.addEventListener("click", function(){
		let pcdFilter = document.getElementById("ps_product_cd").value;
		let pnmFilter = document.getElementById("ps_product_nm").value;
		
		const ps_table = document.getElementById("ps_table");
		const ps_tr = ps_table.getElementsByTagName("tr");
		
		let visibleRows = 0;
		
		// 기존 메시지 제거
	    const existingNoDataRow = document.getElementById("no-data-message");
	    if (existingNoDataRow) {
	        ps_table.removeChild(existingNoDataRow);
	    }
		
		for(let i = 1; i < ps_tr.length; i++){
			const tdpcd = ps_tr[i].getElementsByTagName("td")[0];
			const tdpnm = ps_tr[i].getElementsByTagName("td")[1];
			
			const pcdText = tdpcd.textContent || tdpcd.innerText;
			const pnmText = tdpnm.textContent || tdpnm.innerText;
			
			if(
				(pcdText.indexOf(pcdFilter) > -1 || !pcdFilter) &&
				(pnmText.indexOf(pnmFilter) > -1 || !pnmFilter)
			){
				ps_tr[i].style.display = "";
				visibleRows++;
			}else{
				ps_tr[i].style.display = "none";
			}
		}
		
		// 보이는 행이 없는 경우 메시지 추가
	    if (visibleRows === 0) {
	        const noDataRow = document.createElement("tr");
	        noDataRow.id = "no-data-message"; // ID 설정
	        noDataRow.innerHTML = "<td colspan='3' style='text-align: center;'>등록된 상품이 없습니다.</td>";
	        ps_table.appendChild(noDataRow);
	    }
	})
	
	//초기화 버튼
	ps_resetBtn.addEventListener("click",function(){
		document.getElementById("ps_product_cd").value = "";
		document.getElementById("ps_product_nm").value = "";
		
		const ps_table = document.getElementById("ps_table");
		const ps_tr = ps_table.getElementsByTagName("tr");
		
		for(let i = 1; i < ps_tr.length; i++){	
			ps_tr[i].style.display = "";
		}
	});
});