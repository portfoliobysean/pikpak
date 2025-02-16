document.addEventListener('DOMContentLoaded', function() {
	var rl_reset_btn = document.getElementById('rl_reset_btn');
	var rl_search_btn = document.getElementById('rl_search_btn');
	var rl_start_dt = document.getElementById('rl_start_dt');
	var rl_end_dt = document.getElementById('rl_end_dt');
	
	//초기화 버튼
	rl_reset_btn.addEventListener('click', function() {
		location.href = '/return/return_list';
	});

	//조회 버튼
	rl_search_btn.addEventListener('click', function() {
		if(rl_start_dt.value == "" && rl_end_dt.value != ""){
			alert("날짜를 확인해 주세요.");
		}
		else if(rl_start_dt.value != "" && rl_end_dt.value == ""){
			alert("날짜를 확인해 주세요.");
		}
		else{
			//검색 값
			let process_stF = document.getElementById('rl_return_st').value;
			let product_cdF = document.getElementById('rl_product_cd').value;
			let product_nmF = document.getElementById('rl_product_nm').value;
			let start_dtF = new Date(rl_start_dt.value);
			let end_dtF = new Date(rl_end_dt.value);

			const table = document.getElementById("rl_table");
			const tr = table.getElementsByTagName("tr");

			let visibleRows = 0;
			
			// 기존 메시지 제거
			const existingNoDataRow = document.getElementById("no-data-message");
			if (existingNoDataRow) {
				table.removeChild(existingNoDataRow);
			}

			for (let i = 0; i < tr.length; i++) {
				const tdSt = tr[i].getElementsByTagName("td")[7];
				const tdCd = tr[i].getElementsByTagName("td")[2];
				const tdNm = tr[i].getElementsByTagName("td")[3];
				const tdDt = tr[i].getElementsByTagName("td")[6];

				const StText = tdSt.textContent || tdSt.innerText;
				const CdText = tdCd.textContent || tdCd.innerText;
				const NmText = tdNm.textContent || tdNm.innerText;
				const DtText = tdDt.textContent || tdDt.innerText;

				let orderDate = new Date(DtText.replace(/-/g, '/'));
				//날짜 빈 값
				const isDateInRange = (
					(!rl_start_dt.value && !rl_end_dt.value) || 
					(orderDate >= start_dtF && orderDate <= end_dtF)
				);

				if (
					(StText.indexOf(process_stF) > -1 || !process_stF) &&
					(CdText.indexOf(product_cdF) > -1 || !product_cdF) &&
					(NmText.indexOf(product_nmF) > -1 || !product_nmF) &&
					isDateInRange
				) {
					tr[i].style.display = "";
					visibleRows++;
				} else {
					tr[i].style.display = "none";
				}
			}
			
			// 보이는 행이 없는 경우 메시지 추가
			if (visibleRows === 0) {
				const noDataRow = document.createElement("tr");
				noDataRow.id = "no-data-message"; // ID 설정
				noDataRow.innerHTML = "<td colspan='9' style='text-align: center;'>등록된 주문이 없습니다.</td>";
				table.appendChild(noDataRow);
			}
		}
	});
	
	//클릭 값 가져오기
	const ps_table_body = document.querySelector("#ps_table tbody");
	ps_table_body.addEventListener('click', function(event){
		if(event.target.tagName == 'TD'){
			const row = event.target.parentNode;
			const cells = row.getElementsByTagName('td');
			const cellValues = [];
			
			for(let f = 0; f < cells.length; f++){
				cellValues.push(cells[f].textContent);
			}
			document.getElementById("rl_product_cd").value = cellValues[0];
			document.getElementById("rl_product_nm").value = cellValues[1];
			
			document.getElementById("ps_popup").style.display = "none";
		}
	});

	//날짜
	rl_start_dt.addEventListener('input', dateck)
	rl_end_dt.addEventListener('input', dateck)

	function dateck() {
		if (rl_start_dt.value > rl_end_dt.value && rl_end_dt.value != "") {
			rl_end_dt.value = "";
			alert("날짜를 확인해 주세요.");
		}
	}
	
	//금액
	const c_purchase_pr = document.querySelectorAll(".c_purchase_pr");
	const c_return_price = document.querySelectorAll(".c_return_price");
	
	c_purchase_pr.forEach(function(element){
		const value = Number(element.textContent);
		element.textContent = value.toLocaleString();
	});
	
	c_return_price.forEach(function(element){
		const value = Number(element.textContent);
		element.textContent = value.toLocaleString();
	});
});