export class inreq_list {
	//검색어 조회 ajax
	inreqstate_list() {
		const inreqst_start_date = document.getElementById("inreqst_start_date").value;
		const inreqst_end_date = document.getElementById("inreqst_end_date").value;
		const inreqst_search_pdcd = document.getElementById("search_pd_cd").value;
		const inreqst_search_state = document.getElementById("inreqst_search_state").value;

		if (inreqst_start_date > inreqst_end_date) {
			alert('정상적인 일자를 입력해주세요');
		}
		else if ((inreqst_start_date != "" && inreqst_end_date == "") || (inreqst_start_date == "" && inreqst_end_date != "")) {
			alert('등록일자로 검색 시 모두 입력되어야합니다.');
		}
		else {
			var inreqst_data = {
				"start_date": inreqst_start_date,
				"end_date": inreqst_end_date,
				"product_cd": inreqst_search_pdcd,
				"request_st": inreqst_search_state
			};

			this.search_data = JSON.stringify(inreqst_data);

			fetch("/inreqstate_search", {
				method: "post",
				headers: { "Content-type": "application/json" },
				body: this.search_data
			})
				.then(function(result_data) {
					return result_data.json();
				})
				.then(function(result_res) {
					const tbody = document.querySelector("#irst_tbody");

					tbody.innerHTML = '';
					
					let totalItems = result_res.length;

					result_res.forEach(function(irstate, index) {
						const disabled = ['완료', '거절'].includes(irstate.request_st) ? 'disabled' : '';
						const manageButtonDisabled = disabled ? 'disabled' : '';

	
						const processing = ['진행'].includes(irstate.request_st) ? 'disabled' : '';
						const manageProcessingDisabled = processing ? 'disabled' : '';

						 // 내림차순으로 숫자를 계산
    					let number = totalItems - index;

						const list = `<tr>
        			<td style="text-align: center; width: 3%;">${number}</td>
            		<td style="text-align: center; width: 9%;">${irstate.product_cd}</td>
            		<td style="text-align: center; width: 14%;">${irstate.product_nm}</td>
            		<td style="text-align: center; width: 7%;">${irstate.total_requested_qty}</td>
            		<td style="text-align: center; width: 7%;">${irstate.remaining_qty}</td>
            		<td style="text-align: center; width: 17%;">${irstate.add_req}</td>
            		<td style="text-align: center; width: 11%;">${irstate.hope_dt}</td>
            		<td style="text-align: center; width: 6%;" class="list_inreqst">${irstate.request_st}</td>
            		<td style="text-align: center; width: 11%;">${irstate.request_dt.substring(0, 10)}</td>
													
            		<td style="text-align: center; width: 15%;">
            		<button class="btn btn-primary inreq_delivery" ${manageButtonDisabled}
					data-supplier-cd="${irstate.supplier_cd}"
					data-product-cd="${irstate.product_cd}"
					data-product-nm="${irstate.product_nm}"
					data-product-qty="${irstate.total_requested_qty}"
					data-add-req="${irstate.add_req}"
					data-hope-dt="${irstate.hope_dt}"
					data-request-cd="${irstate.request_cd}"
							
					style="padding-right:5px; padding-left:5px; width:40%;"
					type="button">납품</button>
										
					<button class="btn btn-danger inreq_reject" ${manageButtonDisabled}  ${manageProcessingDisabled}
					style="padding-right:5px; padding-left:5px; width:40%;"
					data-request-idx="${irstate.request_idx}"
					type="button">거절</button>
            		</td>
					</tr>`;

						tbody.innerHTML += list;
					});
					
					// 동적 HTML이 렌더링된 후에 바로 접근
					const list_inreqst = document.getElementsByClassName("list_inreqst");

					// 배열로 변환하여 각 요소에 대해 색상 처리
					Array.from(list_inreqst).forEach(function(state) {
						switch (state.innerText) {
							case "대기":
								state.style.color = "#808080"; // 회색
								break;
							case "진행":
								state.style.color = "#007BFF"; // 파란색
								break;
							case "거절":
								state.style.color = "#DC3545"; // 빨간색
								break;
							case "완료":
								state.style.color = "#28A745"; // 초록색
								break;
						}
					});
				})
				.catch(function(error) {
					//console.log(error);
					alert('데이터 조회에 문제가 발생하였습니다.');
				});
		}
	}



	//입고요청에 대한 거절 버튼 클릭시
	reject_list(inreq_idx) {
		this.idx = "request_idx=" + inreq_idx;

		if (confirm("해당 입고요청을 거절하시겠습니까?")) {
			fetch('/reject_deliverylist', {
				method: "post",
				headers: { "Content-type": "application/x-www-form-urlencoded" },
				body: this.idx
			})
				.then(function(result_data) {
					return result_data.text();
				})
				.then(function(result_res) {
					if (result_res == "ok") {
						alert('해당 입고요청이 거절되었습니다.');
						location.reload();
					}
				})
				.catch(function(error) {
					//console.log(error);
					alert('데이터 조회에 문제가 발생하였습니다.');
				});
		}

	}

	//거절로 상태변화된 리스트에 대해 납품 클릭 못하게 하기


}

export class returnstate_list{
	return_list_search(){
		const return_start_dt = document.getElementById("return_start_dt").value;
		const return_end_dt = document.getElementById("return_end_dt").value;
		const return_pdcd = document.getElementById("search_pd_cd").value;
		
		if (return_start_dt > return_end_dt) {
			alert('정상적인 일자를 입력해주세요');
		}
		else if ((return_start_dt != "" && return_end_dt == "") || (return_start_dt == "" && return_end_dt != "")) {
			alert('등록일자로 검색 시 모두 입력되어야합니다.');
		}
		else {
			var returnstate_data = {
				"start_date": return_start_dt,
				"end_date": return_end_dt,
				"product_cd": return_pdcd
			};

			this.return_search = JSON.stringify(returnstate_data);
			
			fetch("/returnstate_search", {
				method: "post",
				headers: { "Content-type": "application/json" },
				body: this.return_search
			})
				.then(function(result_data) {
					return result_data.json();
				})
				.then(function(result_res) {
					const tbody = document.querySelector("#return_tbody");
					//console.log(result_res);
					
					tbody.innerHTML = '';
					
					let totalItems = result_res.length;
					
					result_res.forEach(function(relist, index) {
					let number = totalItems - index;
						const list = `<tr  class="tooltip-target" data-tooltip="데이터 코드: ${relist.deliver_cd}">
        			<td style="text-align: center; width: 3%;">${number}</td>
            		<td style="text-align: center; width: 11%;">${relist.product_cd}</td>
            		<td style="text-align: center; width: 12%;">${relist.product_nm}</td>
            		<td style="text-align: center; width: 9%;">${relist.exreceiving_qty}</td>
            		<td style="text-align: center; width: 9%;">${relist.d_return_qty}</td>
            		<td style="text-align: center; width: 30%;">${relist.d_return_type}</td>
            		<td style="text-align: center; width: 13%;">${relist.departure_dt.substring(0,10)}</td>
            		<td style="text-align: center; width: 13%;">${relist.d_return_dt.substring(0,10)}</td>
					</tr>`;

						tbody.innerHTML += list;
					});
				})
				.catch(function(error) {
					//console.log(error);
					alert('데이터 조회에 문제가 발생하였습니다.');
				});
				
				
		}
	}
		
}


export class delivery_list {
	////검색어 조회 ajax
	delivery_go_list(){
		
		const delenroll_start_date = document.getElementById("delenroll_start_date").value;
		const delenroll_end_date = document.getElementById("delenroll_end_date").value;
		const delenroll_pdcd = document.getElementById("search_pd_cd").value;
		const delenroll_state = document.getElementById("delenroll_state").value;

		
		if (delenroll_start_date > delenroll_end_date) {
			alert('정상적인 일자를 입력해주세요');
		}
		else if ((delenroll_start_date != "" && delenroll_end_date == "") || (delenroll_start_date == "" && delenroll_end_date != "")) {
			alert('등록일자로 검색 시 모두 입력되어야합니다.');
		}
		else {
			var delienroll_data = {
				"start_date": delenroll_start_date,
				"end_date": delenroll_end_date,
				"product_cd": delenroll_pdcd,
				"deliver_st": delenroll_state
			};

			this.deli_search = JSON.stringify(delienroll_data);
			
			
			fetch("/deliverenroll_search", {
				method: "post",
				headers: { "Content-type": "application/json" },
				body: this.deli_search
			})
				.then(function(result_data) {
					return result_data.json();
				})
				.then(function(result_res) {
					const tbody = document.querySelector("#denroll_tbody");
					//console.log(result_res);
					
					tbody.innerHTML = '';
					window.deli_active_ck = 0; // 활성화된 체크박스 수를 리셋
					
					result_res.forEach(function(delienroll) {
						const delivery = ['배송'].includes(delienroll.deliver_st) ? 'disabled' : '';
						const manageDeliveryDisabled = delivery ? 'disabled' : '';
	
						const DelicheckboxDisabled = delienroll.deliver_st == '배송' ? 'disabled' : '';
				
						if (!DelicheckboxDisabled) {
                			window.deli_active_ck++; // 활성화된 체크박스 카운트
            			}

						const list = `<tr>
        			<td style="text-align: center; width: 3%;">
        			<input type="checkbox" name="del_each_ck" value="${delienroll.deliver_idx}" ${DelicheckboxDisabled}></td>
            		<td style="text-align: center; width: 10%;">${delienroll.product_cd}</td>
            		<td style="text-align: center; width: 12%;">${delienroll.product_nm}</td>
            		<td style="text-align: center; width: 7%;">${delienroll.deliver_qty}</td>
            		<td style="text-align: center; width: 6%;">${delienroll.deliver_size}</td>
            		<td style="text-align: center; width: 10%;">${delienroll.make_dt}</td>
            		<td style="text-align: center; width: 10%;">${delienroll.deliver_dt.substring(0,10)}</td>
            		<td style="text-align: center; width: 7%;" class="list_delienroll">${delienroll.deliver_st}</td>
            		<td style="text-align: center; width: 10%;">${delienroll.departure_dt != null ? delienroll.departure_dt.substring(0, 10) : ''}</td>
													
            		<td style="text-align: center; width: 15%;">
            		<button class="btn btn-danger deliver_manage" ${manageDeliveryDisabled}
					data-request-cd="${delienroll.request_cd}"
					data-deliver-cd="${delienroll.deliver_cd}"
					data-supplier-cd="${delienroll.supplier_cd}"
					data-product-cd="${delienroll.product_cd}"
					data-product-nm="${delienroll.product_nm}"
					data-deliver-qty="${delienroll.deliver_qty}"
					data-deliver-size="${delienroll.deliver_size}"
					data-make-dt="${delienroll.make_dt}"		
					data-deliver-dt="${delienroll.deliver_dt}"
					data-deliver-st="${delienroll.deliver_st}"
					style="padding-right: 10px; padding-left: 10px;"
					type="button">배송</button>
								
					</tr>`;

						tbody.innerHTML += list;
					});
					// 동적 HTML이 렌더링된 후에 바로 접근
					const list_delienroll = document.getElementsByClassName("list_delienroll");

					// 배열로 변환하여 각 요소에 대해 색상 처리
					Array.from(list_delienroll).forEach(function(state) {
						switch (state.innerText) {
							case "대기":
								state.style.color = "#808080"; // 회색
								break;
							case "배송":
								state.style.color = "#28A745"; // 초록색
								break;
						}
					});
					
				})
				.catch(function(error) {
					//console.log(error);
					alert('데이터 조회에 문제가 발생하였습니다.');
				});
				
		}
	}
	

	//납품등록 관리 버튼 클릭시
	decide_delivery() {
		if (frm_decide_deli.departure_dt.value == "") {
			alert('배송일시를 입력해주세요');
		}
		else {
			if (confirm("실제 배송이 완료된 데이터 코드인지 확인해주세요. 배송 확정 하시겠습니까?")) {
				frm_decide_deli.method = "post";
				frm_decide_deli.action = "./insert_exreceiving";
				frm_decide_deli.submit();
			}

		}

		/*	인자로 deliveryData 받았음
			this.exreceiving_data = {
				"request_cd": deliveryData.requestCd,
				"deliver_cd": deliveryData.deliverCd,
				"supplier_cd": deliveryData.supplierCd,
				"product_cd": deliveryData.productCd,
				"exreceiving_qty": deliveryData.deliverQty,
				"exreceiving_size": deliveryData.deliverSize,
				"exreceiving_area": deliveryData.deliverArea,
				"make_dt": deliveryData.makeDt
			}
	
			if (confirm('실제 배송이 완료된 데이터 코드인지 확인해주세요. 배송 확정 하시겠습니까?')) {
				fetch('/insert_exreceiving', {
					method: "post",
					headers: { "Content-type": "application/json" },
					body: JSON.stringify(this.exreceiving_data)
				})
					.then(function(result_data) {
						return result_data.text();
					})
					.then(function(result_res) {
						//console.log(result_res);
						if (result_res == "ok") {
							alert("배송 변경 완료되었습니다.");
							location.reload();
						}
						else {
							alert("데이터베이스 문제로 변경이 되지 못하였습니다.");
						}
					})
					.catch(function(error) {
						console.log(error);
					});
			}
	
		*/
	}

	//전체 체크 누르고 끌 때
	deli_all_ckbox() {
		this.each_ck = document.getElementsByName("del_each_ck"); //개별 체크박스
		this.all_ck_checked = document.getElementById("del_all_ck").checked; //전체 체크박스

		if (this.each_ck.length == 1 && !this.each_ck.disabled) {
			this.each_ck.checked = this.all_ck_checked;
		}
		else {
			for (this.f = 0; this.f < this.each_ck.length; this.f++) {
				if(!this.each_ck[this.f].disabled){
					this.each_ck[this.f].checked = this.all_ck_checked;
				}
				
			}
		}
	}

	//개별 체크 누르고 끌 때
	deli_each_ckbox() {
		this.each_ck = document.getElementsByName("del_each_ck"); //개별 체크박스
		this.ck_count = 0;
		
		if (this.each_ck.length == 1 && this.each_ck.checked == true && !this.each_ck.disabled) {
			this.ck_count++;
			document.getElementById("del_all_ck").checked = this.each_ck.checked;
		}
		else {
			// 모든 체크박스를 반복하면서 활성화된 체크박스의 수와 체크된 체크박스의 수를 센다.
   	 		this.each_ck.forEach(checkbox => {
        		if (!checkbox.disabled && checkbox.checked) { // 체크박스가 비활성화되지 않았을 경우
      				this.ck_count++; // 체크된 체크박스 수 증가
        		}
			});
		
	
			if (this.ck_count == window.deli_active_ck) {
				document.getElementById("del_all_ck").checked = true;
			}
			else {
				document.getElementById("del_all_ck").checked = false;
			}
		
		}
		return this.ck_count;
	}

	//납품등록 리스트 삭제
	delete_delivery() {
		this.names_ea = document.getElementsByName("del_each_ck").length;

		if (this.deli_each_ckbox() == 0) {
			alert('삭제할 데이터를 선택해주세요');
		}
		else if (confirm("정말로 삭제하시겠습니까?")) {

			this.deli_idx_data = new Array();

			if (this.names_ea == 1) {
				this.deli_idx_data.push(frm_delivery_list.del_each_ck.value);
			}
			else {
				for (this.f = 0; this.f < frm_delivery_list.del_each_ck.length; this.f++) {
					if (frm_delivery_list.del_each_ck[this.f].checked == true) {
						this.deli_idx_data.push(frm_delivery_list.del_each_ck[this.f].value);
					}
				}


			}

			frm_delivery_list.deliver_idx.value = this.deli_idx_data.join(",");
			frm_delivery_list.method = "post";
			frm_delivery_list.action = "./delete_deliveryok";
			frm_delivery_list.submit();
		}
	}
	//이거 삭제하고 관련 트리거 걸어볼거야
}

export class delivery_modal {
	delvery_enroll() {
		const deliver_qty = frm_del_modal.deliver_qty.value;
		const make_dt = frm_del_modal.make_dt.value;
		const expect_dt = frm_del_modal.expect_dt.value;
		const deliver_size = frm_del_modal.deliver_size.value;


		if (deliver_qty == "" || make_dt == "" || expect_dt == "" || deliver_size == "") {
			alert('값을 모두 입력해주세요');
		}
		else {
			frm_del_modal.method = "post";
			frm_del_modal.action = "./delivery_enrollok";
			frm_del_modal.submit();
		}

	}
}

export class delivery_page_move {
	go_delivery_return() {
		location.href = "./returnstate";
	}

	go_delivery_enroll() {
		location.href = "./deliveryenroll";
	}

	go_inreq_state() {
		location.href = "./inreqstate";
	}
}