export class receiving_enroll {
	//처음 리스트를 띄우기 위한 ajax
	recv_enroll_list() {
		const type_select = document.getElementById("type_select").value;
		const recv_start_date = document.getElementById("recv_start_date").value;
		const recv_end_date = document.getElementById("recv_end_date").value;
		const recv_spcd = document.getElementById("search_cp_cd").value;
		const recv_pdcd = document.getElementById("search_pd_cd").value;
		const recv_st = document.getElementById("recv_st").value;
		
		if (recv_start_date > recv_end_date) {
			alert('정상적인 일자를 입력해주세요');
		}
		else if ((recv_start_date != "" && recv_end_date == "") || (recv_start_date == "" && recv_end_date != "")) {
			alert('입고요청일자로 검색 시 모두 입력되어야합니다.');
		}
		else {

			var data = {
				"exreceiving_type": type_select,
				"start_date": recv_start_date,
				"end_date": recv_end_date,
				"exreceiving_st": recv_st,
				"supplier_cd": recv_spcd,
				"product_cd": recv_pdcd
			};

			this.search_data = JSON.stringify(data);

			fetch("/receiving_search", {
				method: "post",
				headers: { "Content-type": "application/json" },
				body: this.search_data
			})
				.then(function(result_data) {
					return result_data.json();
				})
				.then(function(result_res) {
					
					const tbody = document.querySelector("#rv_tbody");

					tbody.innerHTML = '';

					let totalItems = result_res.length; // result_res는 반복되는 exrecv 배열
					
					result_res.forEach(function(exrecv, index) {
						const disabled = ['입고'].includes(exrecv.exreceiving_st) ? 'disabled' : '';
						const manageButtonDisabled = disabled ? 'disabled' : '';
						
						  // 내림차순으로 숫자를 계산
    					let number = totalItems - index;
    
						if (type_select == "납품") {
						const list_deliver = `<tr>
        			<td style="text-align: center; width: 3%;">${number}</td>
            		<td style="text-align: center; width: 10%;">${exrecv.supplier_nm}</td>
            		<td style="text-align: center; width: 10%;">${exrecv.product_cd}</td>
            		<td style="text-align: center; width: 10%;">${exrecv.product_nm}</td>
            		<td style="text-align: center; width: 6%;">${exrecv.exreceiving_size}</td>
            		<td style="text-align: center; width: 5%;">${exrecv.exreceiving_type}</td>
            		<td style="text-align: center; width: 5%;">${exrecv.exreceiving_qty}</td>
            		<td style="text-align: center; width: 5%;">${exrecv.return_qty}</td>
            		<td style="text-align: center; width: 12%;">${exrecv.departure_dt.substring(0, 10)}</td>
					<td style="text-align: center; width: 7%;" class="list_recvenroll">${exrecv.exreceiving_st}</td>					
					<td style="text-align: center; width: 12%;">${(exrecv.update_dt != null) ? exrecv.update_dt.substring(0, 10) : ''}</td>
										
            		<td style="text-align: center; width: 15%;">
            		
            		<button style="padding-right: 5px; padding-left: 5px; width: 40%;" class="btn btn-primary decide_inbound" ${manageButtonDisabled}
            		data-make-dt="${exrecv.make_dt}"
            		data-exrecv-size="${exrecv.exreceiving_size}"
            		data-product-cd="${exrecv.product_cd}"
					data-product-nm="${exrecv.product_nm}"
					data-supplier-nm="${exrecv.supplier_nm}"
					data-product-qty="${exrecv.exreceiving_qty}"
					data-return-qty="${exrecv.return_qty}"
					data-exrecv-dt="${exrecv.departure_dt}"
					data-deliver_cd="${exrecv.deliver_cd}"
					data-exrecv-cd="${exrecv.exreceiving_cd}"
					data-supplier-cd="${exrecv.supplier_cd}"
					data-exrecv-type="${exrecv.exreceiving_type}"
					>입고</button>
					
					<button style="padding-right: 5px; padding-left: 5px; width: 40%;" class="btn btn-danger decide_return" ${manageButtonDisabled}
            		
            		data-product-cd="${exrecv.product_cd}"
					data-product-nm="${exrecv.product_nm}"
					data-supplier-nm="${exrecv.supplier_nm}"
					data-product-qty="${exrecv.exreceiving_qty}"
									
					data-exrecv-dt="${exrecv.departure_dt}"		
					data-deliver_cd="${exrecv.deliver_cd}"
					data-exrecv-cd="${exrecv.exreceiving_cd}"
					data-supplier-cd="${exrecv.supplier_cd}">반송</button>
            		</td>
					</tr>`;
						tbody.innerHTML += list_deliver;
					}
					else {
					const list_return = `<tr>
        			<td style="text-align: center; width: 3%;">${number}</td>
            		<td style="text-align: center; width: 10%;">${exrecv.supplier_nm}</td>
            		<td style="text-align: center; width: 10%;">${exrecv.product_cd}</td>
            		<td style="text-align: center; width: 10%;">${exrecv.product_nm}</td>
            		<td style="text-align: center; width: 6%;">${exrecv.exreceiving_size}</td>
            		<td style="text-align: center; width: 5%;">${exrecv.exreceiving_type}</td>
            		<td style="text-align: center; width: 5%;">${exrecv.exreceiving_qty}</td>
            		<td style="text-align: center; width: 5%;"></td>
            		<td style="text-align: center; width: 12%;">${exrecv.processing_dt.substring(0, 10)}</td>		
					<td style="text-align: center; width: 7%;" class="list_recvenroll">${exrecv.exreceiving_st}</td>					
					<td style="text-align: center; width: 12%;">${(exrecv.update_dt != null) ? exrecv.update_dt.substring(0, 10) : ''}</td>									
            		<td style="text-align: center; width: 15%;">
            		
            		<button style="padding-right: 5px; padding-left: 5px; width: 40%;" class="btn btn-primary decide_inbound" ${manageButtonDisabled}
            		data-make-dt="${exrecv.make_dt}"
            		data-exrecv-size="${exrecv.exreceiving_size}"
            		data-product-cd="${exrecv.product_cd}"
					data-product-nm="${exrecv.product_nm}"
					data-supplier-nm="${exrecv.supplier_nm}"
					data-product-qty="${exrecv.exreceiving_qty}"
					data-return-qty=0
					data-exrecv-dt="${exrecv.processing_dt}"
					data-deliver_cd=""
					data-exrecv-cd="${exrecv.exreceiving_cd}"
					data-supplier-cd="${exrecv.supplier_cd}"
					data-exrecv-type="${exrecv.exreceiving_type}"
					>입고</button>
					
					</tr>`;
						tbody.innerHTML += list_return;
					}

					});
					
					// 동적 HTML이 렌더링된 후에 바로 접근
					const list_recvenroll = document.getElementsByClassName("list_recvenroll");

					// 배열로 변환하여 각 요소에 대해 색상 처리
					Array.from(list_recvenroll).forEach(function(state) {
						switch (state.innerText) {
							case "대기":
								state.style.color = "#808080"; // 회색
								break;
							case "입고":
								state.style.color = "#28A745"; // 초록색
								break;
						}
					});

				})
				.catch(function(error) {
					alert('데이터 조회에 문제가 발생하였습니다.');
				});

		}
	}




	//위치정보 가져오기 위한 ajax
	bring_locations(sp_cd) {
		
		
		if (frm_decide_recv.total_qty != frm_decide_recv.return_qty) {
			//supplier_cd에 대한 위치코드 긁어오기
			fetch("/inventory_locations?supplier_cd=" + sp_cd, {
				method: "get"
			})
				.then(function(result_data) {
					return result_data.json();
				})
				.then(function(locations) {
					
					
					var exrecv_size = document.getElementById('exrecv_size');
					var location_select = document.getElementById("location_select");

					function update_location_option() {
						var selected_type = exrecv_size.value;
						location_select.innerHTML = '<option value="">위치 선택</option>'; // 초기화

						locations.forEach(function(location) {
							var type_matches = false;
							if (selected_type == '1유형' && location.location_cd.includes("L1")) {
								type_matches = true;
							} else if (selected_type == '2유형' && (location.location_cd.includes("L2") || location.location_cd.includes("L3"))) {
								type_matches = true;
							} else if (selected_type == '3유형' && location.location_cd.includes("L4")) {
								type_matches = true;
							}

							if (type_matches) {
								var option = document.createElement('option');
								option.value = location.location_cd;
								option.textContent = location.location_cd + " (" + location.current_capacity + "/" + location.max_capacity + ")";
								location_select.appendChild(option);
							}
						});
					}

					exrecv_size.addEventListener('change', update_location_option);

					// 모달이 열릴 때 초기 옵션을 설정
					update_location_option();
				})
				.catch(function(error) {
					alert('데이터 조회에 문제가 발생하였습니다.');
				});

		}



	}

	no_button() {
		// 'recv_state_ck' 클래스를 가진 모든 td 요소를 가져옵니다.
		const state_cells = document.querySelectorAll('.recv_state_ck');

		// 각 상태 셀을 순회하면서 상태 확인
		state_cells.forEach(function(cell) {

			// 상태가 '입고'인 경우
			if (cell.innerText == '입고') {
				const row = cell.closest('tr'); // 현재 셀의 행을 찾음
				const inbound_button = row.querySelector('.decide_inbound'); // '입고' 버튼
				const return_button = row.querySelector('.decide_return'); // '반송' 버튼

				// 버튼들을 비활성화
				inbound_button.disabled = true;
				return_button.disabled = true;
			}

		});
	}

	//입고 등록
	go_recv_enroll() {
		var location_select = document.getElementById("location_select");
		var selected_option = location_select.options[location_select.selectedIndex].text;

		// 현재 용량과 최대 용량 추출
		var capacity_match = selected_option.match(/\((\d+)\/(\d+)\)$/);

		if (frm_decide_recv.inventory_dt.value == "" || frm_decide_recv.location_cd.value == "") {
			alert("입고일자 및 위치코드를 지정해주세요.");
		}
		else if (parseInt(capacity_match[1]) >= parseInt(capacity_match[2])) {
			alert("해당 위치는 사용하실 수 없습니다.");
		}
		else {
			frm_decide_recv.method = "post";
			frm_decide_recv.action = "./inbound_enrollok";
			frm_decide_recv.submit();
		}
	}


	//반송 등록
	go_return_enroll() {
		if (frm_deliver_return.d_return_dt.value == "") {
			alert('반송 일자를 입력해주세요')
		}
		else if (frm_deliver_return.d_return_type.value == "") {
			alert('반송 사유를 입력해주세요');
		}
		else {
			frm_deliver_return.method = "post";
			frm_deliver_return.action = "./deliver_returnok";
			frm_deliver_return.submit();
		}
	}

}




export class product_search_modal {
	//회사명 찾기와 상품명 찾기가 같은 html파일에서 로드되고 있기 때문에, 함수 호출이 다르더라도 id가 충돌나면 둘다 실행되어서 버그 오짐
	pd_paging(pagenum) {
		// 페이지 그룹 계산
		const group_size = 5;
		const current_group = Math.floor((pagenum - 1) / group_size); // 현재 페이지 그룹
		const start_page = current_group * group_size + 1;

		const pd_nm = document.getElementById("pd_nm").value;
		const pd_cd = document.getElementById("pd_cd").value;

		//ajax를 통해 해당 페이지의 데이터 리스트 가져오기
		fetch('/product_paging?page=' + pagenum + '&pd_nm=' + pd_nm + '&pd_cd=' + pd_cd, {
			method: 'get'
		})
			.then(function(result_data) {
				return result_data.json();
			})
			.then(function(result_res) {
				var page_ea = Math.ceil(result_res.pd_total / result_res.page_size); // 페이지 개수 계산
				var pagination = document.querySelector("#page_ul_pd");


				if (result_res.pd_total != 0) {

					// 기존 페이지 번호 비우기 (이전과 다음 버튼은 남기고 페이지 번호만 제거)
					document.querySelectorAll('#page_ul_pd .page-num').forEach(function(item) {
						item.remove();
					});


					// 페이지 번호 생성 (5개 단위로 제한)
					for (var i = start_page; i < start_page + group_size && i <= page_ea; i++) {
						var li = document.createElement('li');
						li.classList.add('page-item', 'page-link', 'page-num');

						li.innerHTML = `<a class="page-link page-num" href="javascript:void(0)" data-page="${i}">${i}</a>`;

						//선택된 데이터에 active추가
						if (i == pagenum) {
							li.classList.add('active'); // 현재 페이지에 active 클래스 추가
						}

						pagination.insertBefore(li, document.getElementById('next_pd'));

					}

					document.getElementById('prev_pd').replaceWith(document.getElementById('prev_pd').cloneNode(true));
					document.getElementById('next_pd').replaceWith(document.getElementById('next_pd').cloneNode(true));


					// Previous 버튼 이벤트 설정
					document.getElementById('prev_pd').addEventListener('click', function() {
						if (pagenum > 1) {
							const prev_group_las = start_page - 1;
							new product_search_modal().pd_paging(Math.max(prev_group_las - group_size + 1, 1));
						}
					});

					// Next 버튼 이벤트 설정
					document.getElementById('next_pd').addEventListener('click', function() {
						if (pagenum < page_ea) {
							const next_group_first = start_page + group_size;
							new product_search_modal().pd_paging(Math.min(next_group_first, page_ea));
						}
					});


					// 이벤트 위임을 사용하여 클릭 이벤트를 처리
					document.querySelector('#page_ul_pd').addEventListener('click', function(event) {
						// 페이지 번호가 클릭된 경우만 처리 (클래스가 'page-num'인 경우)
						if (event.target.classList.contains('page-num')) {
							var pagenum = event.target.getAttribute('data-page');

							// 이미 이벤트가 처리 중인지 확인
							if (!event.target.classList.contains('clicked')) {
								event.target.classList.add('clicked');
								new product_search_modal().pd_paging(pagenum);
							}
						}
					});

					// 매입처 조회결과 업데이트
					let tableBody = document.querySelector("#product_tbody");
					tableBody.innerHTML = ''; // 테이블 내용 초기화
					result_res.pdlist_part.forEach(function(product, index) {
						let tr = document.createElement('tr');
						tr.innerHTML = `<td style="width:10%;">${result_res.pd_total - (pagenum - 1) * result_res.page_size - index}</td>
                                <td style="width:40%; text-align: center;">${product.product_cd}</td>
                                <td style="width:50%; text-align: center;">${product.product_nm}</td>`;

						tr.addEventListener('click', function() {
							document.getElementById("search_pd_cd").value = product.product_cd; // 상품 코드 자동 입력
							document.getElementById("search_pd_nm").value = product.product_nm; // 상품명 자동 입력
							$('#productModal').modal('hide'); // 모달 닫기
						});

						tableBody.appendChild(tr);
					});
				}
				else {
					// 테이블 내용 초기화
					let tableBody = document.querySelector("#product_tbody");
					tableBody.innerHTML = '';

					// 테이블 내용 생성
					let tr = document.createElement('tr');
					tr.innerHTML = `<td colspan='3' style="text-align: center;">조회된 정보가 없습니다.</td>`;
					tableBody.appendChild(tr);

					// 기존 페이지 번호 비우기 (이전과 다음 버튼은 남기고 페이지 번호만 제거)
					document.querySelectorAll('#page_ul_pd .page-num').forEach(function(item) {
						item.remove();
					});

					// 페이지 번호 1개 생성
					var li = document.createElement('li');
					li.classList.add('page-item', 'page-link', 'page-num', 'active');

					li.innerHTML = `<a class="page-link">1</a>`;
					pagination.insertBefore(li, document.getElementById('next_pd'));
				}

			})
			.catch(function(error) {
				alert('데이터 조회에 문제가 발생하였습니다.');
			});

	}
}



export class company_search_modal {
	paging(pagenum) {
		// 페이지 그룹 계산
		const group_size = 5;
		const current_group = Math.floor((pagenum - 1) / group_size); // 현재 페이지 그룹
		const start_page = current_group * group_size + 1;

		const comp_nm = document.getElementById("comp_nm").value;
		const comp_cd = document.getElementById("comp_cd").value;

		//ajax를 통해 해당 페이지의 데이터 리스트 가져오기
		fetch('/company_paging?page=' + pagenum + '&comp_nm=' + comp_nm + '&comp_cd=' + comp_cd, {
			method: 'get'
		})
			.then(function(result_data) {
				return result_data.json();
			})
			.then(function(result_res) {
				var page_ea = Math.ceil(result_res.sp_total / result_res.page_size); // 페이지 개수 계산
				var pagination = document.querySelector("#page_ul_cp");

				if (result_res.sp_total != 0) {
					// 기존 페이지 번호 비우기 (이전과 다음 버튼은 남기고 페이지 번호만 제거)
					document.querySelectorAll('#page_ul_cp .page-num').forEach(function(item) {
						item.remove();
					});


					// 페이지 번호 생성 (5개 단위로 제한)
					for (var i = start_page; i < start_page + group_size && i <= page_ea; i++) {

						var li = document.createElement('li');
						li.classList.add('page-item', 'page-link', 'page-num');

						li.innerHTML = `<a class="page-link page-num" href="javascript:void(0)" data-page="${i}">${i}</a>`;

						//선택된 데이터에 active추가
						if (i == pagenum) {
							li.classList.add('active'); // 현재 페이지에 active 클래스 추가
						}

						pagination.insertBefore(li, document.getElementById('next_cp'));

					}


					/* 이걸 이벤트들보다 먼저 실행시켜야 작동하네...*/
					document.getElementById('prev_cp').replaceWith(document.getElementById('prev_cp').cloneNode(true));
					document.getElementById('next_cp').replaceWith(document.getElementById('next_cp').cloneNode(true));


					// Previous 버튼 이벤트 설정
					document.getElementById('prev_cp').addEventListener('click', function() {
						if (pagenum > 1) {
							const prev_group_las = start_page - 1;
							new company_search_modal().paging(Math.max(prev_group_las - group_size + 1, 1));
						}
					});

					// Next 버튼 이벤트 설정
					document.getElementById('next_cp').addEventListener('click', function() {
						if (pagenum < page_ea) {
							const next_group_first = start_page + group_size;
							new company_search_modal().paging(Math.min(next_group_first, page_ea));
						}
					});


					// 이벤트 위임을 사용하여 클릭 이벤트를 처리
					document.querySelector('#page_ul_cp').addEventListener('click', function(event) {
						// 페이지 번호가 클릭된 경우만 처리 (클래스가 'page-num'인 경우)
						if (event.target.classList.contains('page-num')) {
							var pagenum = event.target.getAttribute('data-page');

							// 이미 이벤트가 처리 중인지 확인
							if (!event.target.classList.contains('clicked')) {
								event.target.classList.add('clicked');
								new company_search_modal().paging(pagenum);
							}
						}
					});

					// 매입처 조회결과 업데이트
					let tableBody = document.querySelector("#supplier_tbody");
					tableBody.innerHTML = ''; // 테이블 내용 초기화
					result_res.splist_part.forEach(function(supplier, index) {
						let tr = document.createElement('tr');
						tr.innerHTML = `<td style="width:10%;">${result_res.sp_total - (pagenum - 1) * result_res.page_size - index}</td>
                                <td style="width:40%; text-align: center;">${supplier.supplier_cd}</td>
                                <td style="width:50%; text-align: center;">${supplier.supplier_nm}</td>`;

						tr.addEventListener('click', function() {
							document.getElementById("search_cp_cd").value = supplier.supplier_cd; // 회사 코드 자동 입력
							document.getElementById("search_cp_nm").value = supplier.supplier_nm; // 회사명 자동 입력
							$('#companyModal').modal('hide'); // 모달 닫기
						});

						tableBody.appendChild(tr);
					});
				}
				else {
					// 테이블 내용 초기화
					let tableBody = document.querySelector("#supplier_tbody");
					tableBody.innerHTML = '';

					// 테이블 내용 생성
					let tr = document.createElement('tr');
					tr.innerHTML = `<td colspan='3' style="text-align: center;">조회된 정보가 없습니다.</td>`;
					tableBody.appendChild(tr);

					// 기존 페이지 번호 비우기 (이전과 다음 버튼은 남기고 페이지 번호만 제거)
					document.querySelectorAll('#page_ul_cp .page-num').forEach(function(item) {
						item.remove();
					});

					// 페이지 번호 1개 생성
					var li = document.createElement('li');
					li.classList.add('page-item', 'page-link', 'page-num', 'active');

					li.innerHTML = `<a class="page-link">1</a>`;
					pagination.insertBefore(li, document.getElementById('next_cp'));
				}
			})
			.catch(function(error) {
				alert('데이터 조회에 문제가 발생하였습니다.');
			});
	}
}


export class inboundreq_list {
	//관리 눌렀을 때
	inreq_management() {
		frm_inreq_manage.method = "post";
		frm_inreq_manage.action = "./inreq_modifyok";
		frm_inreq_manage.submit();
	}


	//검색 조회 눌렀을 때
	inreq_list_search() {

		const start_date = document.getElementById("start_date").value;
		const end_date = document.getElementById("end_date").value;
		const search_cp_cd = document.getElementById("search_cp_cd").value;
		const search_pd_cd = document.getElementById("search_pd_cd").value;
		const search_state = document.getElementById("search_state").value;
		const search_operator = "";

		if (start_date > end_date) {
			alert('정상적인 일자를 입력해주세요');
		}
		else if ((start_date != "" && end_date == "") || (start_date == "" && end_date != "")) {
			alert('입고요청일자로 검색 시 모두 입력되어야합니다.');
		}
		else {
			var data = {
				"start_date": start_date,
				"end_date": end_date,
				"supplier_cd": search_cp_cd,
				"product_cd": search_pd_cd,
				"request_st": search_state,
				"operator_nm": search_operator
			};

			this.search_data = JSON.stringify(data);
			//console.log(this.search_data);

			fetch("/inboundreq_search", {
				method: "post",
				headers: { "Content-type": "application/json" },
				body: this.search_data
			})
				.then(function(result_data) {
					return result_data.json();
				})
				.then(function(result_res) {
					
					const tbody = document.querySelector("#ir_tbody");

					tbody.innerHTML = '';
					window.active_ck = 0; // 활성화된 체크박스 수를 리셋

					result_res.forEach(function(inputreq) {
						const disabled = ['진행', '완료', '거절'].includes(inputreq.request_st) ? 'disabled' : '';
						const manageButtonDisabled = disabled ? 'disabled' : '';
						const checkboxDisabled = inputreq.request_st === '진행' || inputreq.request_st === '완료' || inputreq.request_st === '거절' ? 'disabled' : '';
						if (!checkboxDisabled) {
							window.active_ck++; // 활성화된 체크박스 카운트
						}
						const list = `<tr>
        			<td style="text-align: center; width: 3%;"><input type="checkbox" name="each_ck" value="${inputreq.request_idx}"  ${checkboxDisabled}></td>
            		<td style="text-align: center; width: 6%;">${inputreq.supplier_nm}</td>
            		<td style="text-align: center; width: 7%;">${inputreq.product_cd}</td>
            		<td style="text-align: center; width: 10%;">${inputreq.product_nm}</td>
            		<td style="text-align: center; width: 7%;">${inputreq.product_qty}</td>
            		<td style="text-align: center; width: 25%;">${inputreq.add_req}</td>
            		<td style="text-align: center; width: 12%;">${inputreq.hope_dt}</td>
            		<td style="text-align: center; width: 5%;" class="list_inboundreq">${inputreq.request_st}</td>
            		<td style="text-align: center; width: 9%;">${inputreq.request_dt.substring(0, 10)}</td>
										
					<td style="text-align: center; width: 9%;">${(inputreq.update_dt != null) ? inputreq.update_dt.substring(0, 10) : ''}</td>
										
            		<td style="text-align: center; width: 7%;">
            		<button style="padding-right:10px; padding-left:10px;" class="btn btn-primary inreq_manage" ${manageButtonDisabled}
            		data-product-cd="${inputreq.product_cd}"
					data-product-nm="${inputreq.product_nm}"
					data-supplier-nm="${inputreq.supplier_nm}"
					data-product-qty="${inputreq.product_qty}"
					data-add-req="${inputreq.add_req}"
					data-hope-dt="${inputreq.hope_dt}"
					data-request-idx="${inputreq.request_idx}">관리</button>
            		</td>
					</tr>`;

						tbody.innerHTML += list;
					});
					
					// 동적 HTML이 렌더링된 후에 바로 접근
					const list_inboundreq = document.getElementsByClassName("list_inboundreq");

					// 배열로 변환하여 각 요소에 대해 색상 처리
					Array.from(list_inboundreq).forEach(function(state) {
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
					alert('데이터 조회에 문제가 발생하였습니다.');
				});
		}

	}



	//전체 체크 누르고 끌 때
	all_ckbox() {
		this.each_ea = document.getElementsByName("each_ck"); //리스트 개수
		this.all_ck_checked = document.getElementById("all_ck").checked;

		if (this.each_ea.length == 1 && !this.each_ea.disabled) {
			frm_inreq_list.each_ck.checked = this.all_ck_checked;
		}
		else {
			for (this.f = 0; this.f < this.each_ea.length; this.f++) {
				if (!this.each_ea[this.f].disabled) {
					this.each_ea[this.f].checked = this.all_ck_checked;
				}
				//frm_inreq_list.each_ck[this.f].checked = this.all_ck_checked;
			}
		}
	}

	//개별 체크 누르고 끌 때
	each_ckbox() {
		const each_ck = document.getElementsByName("each_ck"); //리스트 개수
		var ck_count = 0; //체크여부

		if (each_ck.length == 1 && each_ck.checked == true && !each_ck.disabled) {
			ck_count++;
			document.getElementById("all_ck").checked = true;
		}
		else {
			// 모든 체크박스를 반복하면서 활성화된 체크박스의 수와 체크된 체크박스의 수를 센다.
			each_ck.forEach(checkbox => {
				if (!checkbox.disabled && checkbox.checked) { // 체크박스가 비활성화되지 않았을 경우
					ck_count++; // 체크된 체크박스 수 증가
				}


			});

		}
		if (ck_count == window.active_ck) {
			document.getElementById("all_ck").checked = true;
		}
		else {
			document.getElementById("all_ck").checked = false;
		}
		return ck_count;
	}

	delete_data() {
		const each_ck = document.getElementsByName("each_ck"); //리스트 개수
			
		if (this.each_ckbox() == 0) {
			alert('삭제할 데이터를 선택해주세요');
		}
		else if (confirm("정말로 삭제하시겠습니까?")) {
			this.idx_data = new Array();
			
			if(each_ck.length == 1 && frm_inreq_list.each_ck.checked == true){
				this.idx_data.push(frm_inreq_list.each_ck.value);
			}
			else{
				for (this.f = 0; this.f < frm_inreq_list.each_ck.length; this.f++) {
				if (frm_inreq_list.each_ck[this.f].checked == true) {
					this.idx_data.push(frm_inreq_list.each_ck[this.f].value);
				}
			}
			}
			

			frm_inreq_list.request_idx.value = this.idx_data.join(",");
			
			frm_inreq_list.method = "post";
			frm_inreq_list.action = "./delete_inreqok";
			frm_inreq_list.submit();
		}
	}
}


export class page_move {
	go_inboundreq() {
		location.href = "./inboundreq"
	}

	go_recvenroll() {
		location.href = "./recvenroll"
	}
}

export class inboundreq_enroll {
	//등록을 위해 값 날리기
	submit_data() {
		if (frm_inreq_modal.product_cd.value == "") {
			alert('상품코드를 입력해주세요');
		}
		else if (frm_inreq_modal.product_qty.value == "") {
			alert('상품수량을 입력해주세요');
		}
		else if (isNaN(frm_inreq_modal.product_qty.value) == true) {
			alert('상품수량은 숫자로 입력해주세요');
		}
		else if (frm_inreq_modal.hope_dt.value == "") {
			alert('희망 납기일자를 선택해주세요 ');
		}
		else {
			frm_inreq_modal.method = "post";
			frm_inreq_modal.action = "./inreq_enrollok";
			frm_inreq_modal.submit();
		}
	}
}