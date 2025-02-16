var allData = [];  // 전체 데이터를 저장할 전역 변수

$(document).ready(function() {
	$("#openDaumPostcode").on("click", function() {
		fn_openDaumPostcode();
	});
	$("#registerClientBtn").on("click", function() {
		fn_registerClient();
	});
	$("#businessNumber").inputmask("999-99-99999");
	$("#corporationNumber").inputmask("999999-9999999");

	fn_clientlist();  // 페이지 로드 시 전체 데이터를 불러옴

	// 필터 옵션이 변경될 때마다 실행되는 함수
	$("#clientTypeFilter").on("change", function() {
		var selectedValue = $(this).val();
		filterAndRender(selectedValue);  // 필터링 후 렌더링
	});
	$("#supplier_initial").on("input", function() {
         var input = $(this).val();

    // 영어 대문자만 남기고 나머지 제거 (한글, 특수 문자, 숫자 등 제거)
    var newValue = input.replace(/[^A-Za-z]/g, '').toUpperCase();

    // 새로운 값을 입력 필드에 반영
    $(this).val(newValue);

    // 한글이 입력되었으면 경고 메시지 출력
    if (/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/.test(input)) {
        alert("영어 대문자만 입력 가능합니다.");
    }
    });
	
	$("#comsearchBtn").on("click", function() {
		fn_searchcompany();
	});
	$("#dupChkBtn").on("click", function(){
		fn_dupChkBtn();
	});
	$("#searchClientBtn").on("click", function() {
		fn_searchClient(); // 조회 함수 호출
	});
	$("#registerClientModal").on("hidden.bs.modal", function() {
		// 체크박스를 모두 해제
		$("#client_list tbody input[type='checkbox']").prop("checked", false);
	});
	$("#registerClientModal").on("hidden.bs.modal", function() {
		// 페이지 새로고침
		location.reload();
	});
	 // 수정 버튼 클릭 이벤트
    $(document).on("click", "#editClientBtn", function() {
        var checkedRow = $("#client_list tbody input[type='checkbox']:checked").closest("tr");

        if (checkedRow.length == 1) {
            var clientId = checkedRow.find("td:eq(1)").text(); // 선택된 업체코드
            var clientType = checkedRow.data("type");  // 선택된 업체 구분 (제조사 또는 고객사)

            // 선택한 항목을 수정하는 함수 호출
            fn_openEditModal(clientId, clientType);
        } else if (checkedRow.length > 1) {
            alert("한 번에 하나의 항목만 수정할 수 있습니다.");
        } else {
            alert("수정할 항목을 선택해주세요.");
        }
    });

    // 더블클릭 이벤트
    $(document).on("dblclick", "#client_list tbody tr", function() {
        var clientId = $(this).find("td:eq(1)").text();
        var clientType = $(this).data("type");
        fn_openEditModal(clientId, clientType);
    });
});

function fn_dupChkBtn(){
	
	var supplier_initial = $("#supplier_initial").val();
	
	if(!supplier_initial){
		alert("제조사 이니셜을 입력하세요");
		return;
	}
	$.ajax({
		url : "/client/dupChk",
		type : "post",
		contentType : "application/json",
		data : JSON.stringify({ 
			supplier_initial: supplier_initial 
			}),
		success : function(response){
			if(response.success_yn == "success"){
				alert(response.message);
				return;
			}
			else{
				alert(response.message);
				return;
			}
			
		}
	});
}


function fn_openEditModal(clientId, clientType) {
    // 서버에 clientId와 clientType을 전송하여 데이터를 조회
    
    $.ajax({
        url: "/client/getclient",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ clientId: clientId, clientType: clientType }),
        success: function(response) {
            // 서버에서 가져온 데이터를 모달창에 채우기
            $("#companyCode").val(response.cd);
            $("#companyName").val(response.nm);
            $("#businessNumber").val(response.regno);
            $("#corporationNumber").val(response.regno2);
            $("#representativeName").val(response.rep_nm);
            $("#industry").val(response.business_type);
            $("#sector").val(response.item_type);
            $("#contact").val(response.tel);
            $("#postalCode").val(response.post);
            $("#address").val(response.addr);
            $("#detailedAddress").val(response.addr2);
            $("#email").val(response.email);
            $("#supplier_initial").val(response.supplier_initial);

  
  
            // 라디오 버튼 체크
            if (response.type == "supplier") {
                $("#supplier").prop("checked", true);
                $("#supplierInitialField").show();
            } else if (response.type == 'vendor') {
                $("#vendor").prop("checked", true);
                  $("#supplierInitialField").hide();
            }
             // 라디오 버튼을 비활성화 (수정 모드에서는 선택 불가)
            $("#supplier").prop("disabled", true);
            $("#vendor").prop("disabled", true);

            // 모달창 띄우기
            $("#registerClientModal").modal("show");

            // 모달창 제목을 "거래처 수정"으로 변경
            $("#registerClientModalLabel").text("거래처 정보");

            // 버튼 텍스트를 "수정"으로 변경하고, 수정 처리 함수로 연결
            $("#registerClientBtn").text("수정");
            $("#registerClientBtn").off("click").on("click", function() {
                fn_updateClient(clientId);  // 수정 처리 함수 호출
            });
        },
        error: function() {
            alert("데이터를 불러오는데 실패했습니다.");
        }
    });
}

function fn_searchClient() {
	var fromDate = $("#fromDate").val() || null;
    var toDate = $("#toDate").val() || null;
	var clientType = $("#clientTypeFilter").val();
	var companyCode = $("#companyCodeInput").val() || null;
	var companyName = $("#companyNameInput").val() || null;

	var searchData = {
		fromDate: fromDate ? fromDate : null,
		toDate: toDate ? toDate : null,
		clientType: clientType || '',
		companyCode: companyCode || '',
		companyName: companyName || ''
	};


	$.ajax({
		url: "/client/search",
		type: "POST",
		contentType: "application/json",
		data: JSON.stringify(searchData),
		success: function(response) {
			if (response.length > 0) {
				var transformedData = response.map(function(client) {
					return {
						code: client.companyCode,
						name: client.companyName,
						regno: client.businessNumber,
						regno2: client.corporationNumber,
						rep_nm: client.representativeName,
						business_type: client.industry,
						item_type: client.sector,
						tel: client.contact,
						post: client.postalCode,
						addr: client.address,
						addr2: client.detailedAddress,
						email: client.email,
						registrated: client.registrationDate,
						type: client.clientType
					};
				});
				renderlist(transformedData);
			} else {
				alert("조건에 맞는 결과가 없습니다.");
				$("#client_list tbody").html("<tr><td colspan='14'>검색 결과가 없습니다.</td></tr>");
			}
		},
		error: function() {
			alert("조회 중 오류가 발생했습니다.");
		}
	});
}


function fn_searchcompany() {
	var companyName = $("input[placeholder='업체명']").val();  // 검색어 입력
	if (companyName == "") {
		alert("업체명을 입력하세요.");
		return;
	}

	// 서버로 AJAX 요청 (업체명 검색)
	$.ajax({
		url: "/client/searchcompanyname",  // 서버의 검색 API URL
		type: "POST",
		contentType: "application/json",
		data: JSON.stringify({ companyName: companyName }),  // 입력된 업체명을 서버로 전송
		success: function(response) {


			// supplierList와 vendorList를 합칩니다.
			var combinedList = response.supplierList.concat(response.vendorList);
			var modalContent = "";

			if (combinedList.length > 0) {
				combinedList.forEach(function(company) {
					modalContent += `
                        <tr>
                            <td>${company.companyCode}</td>
                            <td style='text-align: left;'>${company.companyName}</td>
                            <td>${company.businessNumber}</td>
                            <td style='text-align: left;'>${company.address}</td>
                            <td>${company.companyType == 'supplier' ? '제조사' : '고객사'}</td>
                            <td><button class="select-company-btn btn btn-info" style="line-height: 10px; text-align: center;" data-id="${company.companyCode}" data-name="${company.companyName}">선택</button></td>
                        </tr>`;
				});
			} else {
				modalContent = "<tr><td colspan='5'>검색 결과가 없습니다.</td></tr>";
			}

			// 모달 바디에 결과 HTML 삽입
			$("#searchResults tbody").html(modalContent);
			// 모달창 띄우기
			$("#searchModal").modal("show");

			// 선택 버튼 클릭 이벤트
			$(".select-company-btn").on("click", function() {
				var selectedCompanyCode = $(this).data("id");   // 업체코드
				var selectedCompanyName = $(this).data("name");  // 업체명

				// 선택된 업체코드와 업체명을 input 필드에 자동으로 입력
				$("input[placeholder='업체코드']").val(selectedCompanyCode);
				$("input[placeholder='업체명']").val(selectedCompanyName);

				// 모달 닫기
				$("#searchModal").modal("hide");
			});
		},
		error: function() {
			alert("데이터를 불러오는데 실패했습니다.");
		}
	});
}




function fn_updateClient() {
	var companyCode = $("#companyCode").val();
	var companyName = $("#companyName").val();
	var businessNumber = $("#businessNumber").val();
	var corporationNumber = $("#corporationNumber").val();
	var representativeName = $("#representativeName").val();
	var industry = $("#industry").val();
	var sector = $("#sector").val();
	var contact = $("#contact").val();
	var postalCode = $("#postalCode").val();
	var address = $("#address").val();
	var detailedAddress = $("#detailedAddress").val();
	var email = $("#email").val();
	var supplier_initial = $("#supplier_initial").val();


	var valradio = $("input[name='category']:checked").val(); // supplier or vendor

	var result = confirm("수정하시겠습니까?");

	if (result) {
		var clientData = {
			cd: companyCode,
			nm: companyName,
			regno: businessNumber,
			regno2: corporationNumber,
			rep_nm: representativeName,
			business_type: industry,
			item_type: sector,
			tel: contact,
			post: postalCode,
			addr: address,
			addr2: detailedAddress,
			email: email,
			supplier_initial : supplier_initial
		};
		var url = valradio == "supplier" ? "/client/updatesupplier" : "/client/updatevendor";  // 업데이트 URL 설정
		$.ajax({
			url: url,
			type: "post",
			contentType: "application/json",
			data: JSON.stringify(clientData),
			success: function(response) {
				if (response.success_yn == "success") {
					alert(response.message);
					$("#registerClientModal").modal("hide");
					location.reload(); // 페이지 새로고침
				} else {
					alert(response.message);
					$("#registerClientModal").modal("hide");
					location.reload(); // 페이지 새로고침
				}
			}
		});
	}
}


// 전체 데이터를 가져오는 함수 (기존 코드와 동일)
function fn_clientlist() {
	$.ajax({
		url: "/client/list",
		type: "post",
		contentType: "application/json",
		dataType: "json",
		success: function(response) {

			// 전체 데이터를 전역 변수에 저장
			allData = response;
			renderlist(allData);  // 처음엔 전체 데이터를 렌더링
		}
	});
}

// 필터링과 렌더링을 담당하는 함수
function filterAndRender(filterType) {
	var filteredData;
	if (filterType == "all") {
		filteredData = allData;  // 전체 데이터 보여주기
	} else if (filterType == "supplier") {
		filteredData = allData.filter(function(client) {
			return client.type == "supplier";  // 제조사만 필터링
		});
	} else if (filterType == "vendor") {
		filteredData = allData.filter(function(client) {
			return client.type == "vendor";  // 고객사만 필터링
		});
	}

	renderlist(filteredData);  // 필터링된 데이터를 렌더링
}

// 서버에서 받아온 데이터를 테이블에 렌더링하는 함수 (기존 코드와 동일)
function renderlist(data) {
	var info_html = "";

	data.forEach(function(client) {
		var date = client.registrated ? new Date(client.registrated) : new Date();
		var formattedDate = date.getFullYear() + '-'
			+ ('0' + (date.getMonth() + 1)).slice(-2) + '-'
			+ ('0' + date.getDate()).slice(-2);

		info_html += "<tr style='height: 10px;' data-type='" + client.type + "'>";
		info_html += "<td class='text-center'><input type='checkbox' style='text-align: center; vertical-align: middle;'></td>";
		info_html += "<td style='vertical-align: middle;'>" + client.code + "</td>";
		info_html += "<td style='vertical-align: middle; text-align:left;'>" + client.name + "</td>";
		info_html += "<td style='vertical-align: middle;'>" + client.regno + "</td>";
		info_html += "<td style='vertical-align: middle;'>" + client.regno2 + "</td>";
		info_html += "<td style='vertical-align: middle;'>" + client.rep_nm + "</td>";
		info_html += "<td style='vertical-align: middle; text-align:left;'>" + client.business_type + "</td>";
		info_html += "<td style='vertical-align: middle; text-align:left;'>" + client.item_type + "</td>";
		info_html += "<td style='vertical-align: middle;'>" + client.tel + "</td>";
		info_html += "<td style='vertical-align: middle;'>" + client.post + "</td>";
		info_html += "<td style='text-align:left;'>" + client.addr + ' ' + client.addr2 + "</td>";
		info_html += "<td style='vertical-align: middle; text-align:left;'>" + client.email + "</td>";
		info_html += "<td style='vertical-align: middle;'>" + formattedDate + "</td>";
		info_html += "<td class='text-center'><button type='button' class='btn btn-secondary btn-xs manage-btn' style='line-height: 10px; text-align: center;' >관리</button></td>";
		info_html += "</tr>";
	});

	// 테이블 본문에 생성된 HTML을 삽입
	$("#client_list tbody").html(info_html);

	$(".manage-btn").on("click", function() {
		var row = $(this).closest("tr");  // 버튼이 속한 행 가져오기
		row.find("input[type='checkbox']").prop("checked", true);  // 해당 행의 체크박스 체크
		$("#editClientBtn").trigger("click");  // 수정 버튼 클릭 이벤트 트리거
	});
}

function fn_openDaumPostcode() {
	new daum.Postcode({
		oncomplete: function(data) {
			// 우편번호와 주소 정보를 받아서 입력 필드에 채워줍니다.
			document.getElementById("postalCode").value = data.zonecode; // 우편번호
			document.getElementById("address").value = data.address;    // 기본 주소
		}
	}).open();
}

function fn_registerClient() {

	$("#supplier").prop("disabled", false);
	$("#vendor").prop("disabled", false);

	var clientType = $("input[name='category']:checked").val();  // supplier or vendor
	var companyCode = $("#companyCode").val();  // 자동으로 입력된 업체 코드
	var companyName = $("#companyName").val();
	var businessNumber = $("#businessNumber").val();
	var corporationNumber = $("#corporationNumber").val();
	var representativeName = $("#representativeName").val();
	var industry = $("#industry").val();
	var sector = $("#sector").val();
	var contact = $("#contact").val();
	var postalCode = $("#postalCode").val();
	var address = $("#address").val();
	var detailedAddress = $("#detailedAddress").val();
	var email = $("#email").val();
	var supplier_initial = $("#supplier_initial").val();

	var result = confirm("등록하시겠습니까?");

	if (result) {
		var clientData = {
			cd: companyCode,
			nm: companyName,
			regno: businessNumber,
			regno2: corporationNumber,
			rep_nm: representativeName,
			business_type: industry,
			item_type: sector,
			tel: contact,
			post: postalCode,
			addr: address,
			addr2: detailedAddress,
			email: email,
			supplier_initial : supplier_initial
		};

		var url = clientType == "supplier" ? "/client/regsupplier" : "/client/regvendor";
		$.ajax({
			url: url,
			type: "post",
			contentType: "application/json",
			data: JSON.stringify(clientData),
			success: function(response) {
				if (response.success_yn == "success") {
					alert(response.message);
					$("#registerClientModal").modal("hide");
					location.reload();
				} else {
					alert(response.message);
				}
			}
		});
	}
}
$(document).ready(function() {
	// 라디오 버튼이 선택될 때 이벤트 처리
	$("input[name='category']").on("change", function() {
		var clientType = $(this).val();  // 선택된 라디오 버튼의 값을 가져옴

		// 서버에 업체 코드를 요청하는 AJAX
		$.ajax({
			url: "/client/getLastCompanyCode",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify({ clientType: clientType }),
			success: function(response) {
				// 서버에서 받은 새로운 업체 코드를 input 필드에 자동으로 입력
				$("#companyCode").val(response.newCode);
			},
			error: function() {
				alert("업체 코드를 가져오는 중 오류가 발생했습니다.");
			}
		});
	});
});

$(document).ready(function() {
    // 구분 라디오 버튼이 변경될 때마다 실행
    $("input[name='category']").on("change", function() {
        if ($("#supplier").is(":checked")) {
            // 제조사가 선택된 경우, 제조사 이니셜 필드를 보여줍니다.
            $("#supplierInitialField").show();
        } else {
            // 제조사가 선택되지 않은 경우, 제조사 이니셜 필드를 숨깁니다.
            $("#supplierInitialField").hide();
        }
    });
});
