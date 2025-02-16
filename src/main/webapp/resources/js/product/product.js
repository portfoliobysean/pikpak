$(document).ready(function() {
    // 외부 모달창에서 검색 버튼 클릭
    $("#comsearchBtn").on("click", function() {
        fn_searchCompany("input[placeholder='업체코드']", "input[placeholder='업체명']");
    });

  /*  // 신규 등록 모달 안의 검색 버튼 클릭
    $("#registerProductModal #comsearchBtn").on("click", function() {
        fn_searchCompany("#registerProductModal #supplier_cd", "#registerProductModal #supplier_nm");
    });*/

    // 모달의 z-index 조정 로직은 성공적으로 모달을 연 후에만 수행하도록 처리
    $("#searchModal").on("show.bs.modal", function() {
        $("#searchModal").css("z-index", parseInt($("#registerProductModal").css("z-index")) + 10);
        $(".modal-backdrop").css("z-index", parseInt($("#registerProductModal").css("z-index")) + 9);
    });

    // 모달 닫힐 때 z-index 초기화
    $("#searchModal").on("hidden.bs.modal", function() {
        $(".modal-backdrop").remove();
        $("#searchModal").css("z-index", '');
    });
    
    $("#registerProductModal").on("hidden.bs.modal", function() {
		// 모달창 닫힐 때 페이지 새로고침
		location.reload();
	});
	$("#registerProductBtn").on("click", function(){
		fn_registerProduct();
	});
	$("#searchProductBtn").on("click", function() {
   		fn_searchProduct();  // 조회 함수 호출
	});
		fn_productList();

	$(document).on("dblclick", "#product_list tbody tr", function() {
		var product_cd = $(this).find("td:eq(1)").text();
		fn_openEditModal(product_cd);
	});
	 $('#purchase_pr').mask('000,000,000,000', {reverse: true});
});
$(document).on("click", "#editProductBtn", function()	{

	var checkedRow = $("#product_list tbody input[type='checkbox']:checked").closest("tr");
	
	if(checkedRow.length == 1){
		var product_cd = checkedRow.find("td:eq(1)").text();
		
		fn_openEditModal(product_cd);
	} else if(checkedRow.length > 1 ){
		alert("한 번에 하나의 항목만 수정할 수 있습니다.");
	} else{
		alert("수정할 항목을 선택해주세요");
	}	
});

function fn_searchProduct() {
 	var fromDate = $("#fromDate").val() || null;
    var toDate = $("#toDate").val() || null;
    var supplier_cd = $("#companyCodeInput").val() || null;
    var supplier_nm = $("#companyNameInput").val() || null;
    var productType = $("#productTypeFilter").val();
    var searchKeyword = $("#searchkeyword").val() || null;
    


    // 날짜만 있을 때도 조회가 가능하게 하기 위해 조건들을 추가합니다.
    var searchData = {
        fromDate: fromDate ? fromDate : null,
        toDate: toDate ? toDate : null,
        supplier_cd: supplier_cd || '',
        supplier_nm: supplier_nm || '',
        productType: productType || 'all',
        searchKeyword: searchKeyword || '',
    };
  

    $.ajax({
        url: "/product/search",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(searchData),
        success: function(response) {
            if (response.length > 0) {
                renderlist(response);  // 검색 결과 렌더링
            } else {
                $("#product_list tbody").html("<tr><td colspan='14'>검색 결과가 없습니다.</td></tr>");
            }
        },
        error: function() {
            alert("검색에 실패했습니다.");
        }
    });
}


function fn_openEditModal(product_cd) {
    $.ajax({
        url : "/product/getproduct",
        type : "post",
        contentType : "application/json",
        data : JSON.stringify({product_cd : product_cd}),
        success : function(response) {
            // 기존의 필드들 값 설정
            $("#supplier_cd").val(response.supplier_cd);
            $("#supplier_nm").val(response.supplier_nm);
            $("#product_nm").val(response.product_nm);
            $("#product_cd").val(response.product_cd);
            $("#purchase_pr").val(response.purchase_pr);
            $("#product_sz").val(response.product_sz);
            $("#product_wt").val(response.product_wt);
            $("#packaging_unit").val(response.packaging_unit);
            $("#safetyinventory_qty").val(response.safetyinventory_qty);
            $("#product_log").val(response.product_log);
            $("#visibility_yn").val(response.visibility_yn);
            
            // 공급자명 읽기 전용으로 설정
            $("#supplier_nm").prop("readonly", true);

            // visibility_yn 값이 있으면 설정
            if (response.visibility_yn) {
                $("#visibility_yn").val(response.visibility_yn);
            }

            // visibility_yn 필드 보이기
            $("#visibilityField").show();

            // 콘솔에서 최종 수정일자와 수정자 확인
            console.log("최종 수정일자: ", response.lastmodified_at);
            console.log("수정자: ", response.update_by);
            
            // 최종 수정일자와 수정자가 존재하면 화면에 표시
            if (response.lastmodified_at && response.update_by) {
                // lastmodified_at을 적절한 포맷으로 변환하여 표시
                var formattedDate = new Date(response.lastmodified_at).toISOString().slice(0, 16).replace("T", " ");
                $("#lastModifiedDate").text("최종수정일자: " + formattedDate);
                $("#modifiedBy").text("수정자: " + response.update_by);
                
                // 숨겨져 있던 div를 보이게 설정
                $("#lastModifiedInfo").css("display", "block");
            }

            // 모달창 띄우기
            $('#registerProductModal').modal('show');

            // 모달창 제목을 "상품 수정"으로 변경
            $("#registerProductModalLabel").text("상품 수정");

            // 버튼 텍스트를 "수정"으로 변경하고, 수정 처리 함수로 연결
            $("#registerProductBtn").text("수정");
            $("#registerProductBtn").off("click").on("click", function() {
                fn_updateProduct(product_cd);  // 수정 처리 함수 호출
            });
        },
        error: function(xhr, status, error) {
            console.error("AJAX 요청 실패: ", status, error);
        }
    });
}

function fn_updateProduct(){
	
	var supplier_cd = $("#supplier_cd").val();
	var supplier_nm = $("#supplier_nm").val();
	var product_nm = $("#product_nm").val();
	var product_cd = $("#product_cd").val();
	var purchase_pr = $("#purchase_pr").val().replace(/,/g, '');
	var product_sz = $("#product_sz").val();
	var product_wt = $("#product_wt").val();
	var packaging_unit = $("#packaging_unit").val();
	var safetyinventory_qty = $("#safetyinventory_qty").val();
	var product_log = $("#product_log").val();
	var visibility_yn = $("#visibility_yn").val();
	
	var result = confirm("상품을 수정하시겠습니까?");
	
	if(result){
		var productData = {
				supplier_cd : supplier_cd, 
				supplier_nm : supplier_nm, 
				product_nm : product_nm,  
				product_cd  :  product_cd, 
				purchase_pr :  purchase_pr, 
				product_sz  :   product_sz,  
				product_wt   : product_wt, 
				packaging_unit :  packaging_unit, 
				safetyinventory_qty :  safetyinventory_qty, 
				product_log  : product_log,
				visibility_yn : visibility_yn,
		}
		$.ajax({
			url : "/product/updateproduct",
			type : "post",
			contentType : "application/json",
			data : JSON.stringify(productData),
			success : function(response){
				if(response.success_yn == "success"){
					alert(response.message);
					$("#registerProductModal").modal("hide");
					location.reload(); 
				}
				else{
					alert(response.message);
					$("#registerProductModal").modal("hide");
					location.reload();
				}
				
			}
		});
	}
}

function fn_productList(){
	
	$.ajax({
		url : "/product/list",
		type : "post",
		contentType : "application/json",
		dataType : "json",
		success : function(response){
			currentData = response;
			renderlist(currentData);
		}
	});
}

function renderlist(data){
	
	var info_html = "";
	
	for(var i = 0; i < data.length; i++){
		var date = new Date(data[i].registered_dt);
		var formattedDate = date.getFullYear() + '-'
			+ ("0" + (date.getMonth() + 1)).slice(-2) + '-'
			+ ("0" + date.getDate()).slice(-2);
		info_html += "<tr style='height: 10px;'>";
		info_html += "<td class='text-center'><input type='checkbox' style='text-align: center; vertical-align: middle;'></td>";
		info_html += "<td style='vertical-align: middle;'>"+ data[i].product_cd +"</td>";
		info_html += "<td style='vertical-align: middle; text-align: left;'>"+ data[i].product_nm +"</td>";
		info_html += "<td style='vertical-align: middle; text-align: left;'>"+ data[i].supplier_nm +"</td>";
		info_html += "<td style='vertical-align: middle; text-align: center;'>"+ data[i].supplier_cd +"</td>";
		info_html += "<td style='vertical-align: middle; text-align: right;'>"+ Number(data[i].purchase_pr).toLocaleString() +"원</td>";
		info_html += "<td style='vertical-align: middle; text-align: center;'>"+ (data[i].product_sz ||'-') +"</td>";
		info_html += "<td style='vertical-align: middle; text-align: right;'>"+ (data[i].product_wt ||'-') +"kg</td>";
		info_html += "<td style='vertical-align: middle;'>"+ (data[i].packaging_unit ||'-') +"</td>";
		info_html += "<td style='vertical-align: middle;'>"+ (data[i].safetyinventory_qty ||'-') +"</td>";
		info_html += "<td style='vertical-align: middle;'>"+ data[i].operator_nm +"</td>";
		info_html += "<td style='vertical-align: middle;'>"+ formattedDate +"</td>";
		info_html += "<td style='vertical-align: middle;'>"+ data[i].visibility_yn +"</td>";
		info_html += "<td style='vertical-align: middle; text-align: left;'>"+ (data[i].product_log ||'-')+"</td>";
		info_html += "<td class='text-center'><button type='button' class='btn btn-secondary btn-xs manage-btn' style='line-height: 10px; text-align: center;' >관리</button></td>";
		info_html += "<tr>";
		
	}
	 $("#product_list tbody").html(info_html);
	
	 $(".manage-btn").on("click", function() {
		var row = $(this).closest("tr");  // 버튼이 속한 행 가져오기
		row.find("input[type='checkbox']").prop("checked", true);  // 해당 행의 체크박스 체크
		$("#editProductBtn").trigger("click");  // 수정 버튼 클릭 이벤트 트리거
	});
}

// 공통 검색 함수
function fn_searchCompany(companyCodeSelector, companyNameSelector, callback) {
    var companyName = $(companyNameSelector).val();  // 입력된 업체명
    if (companyName == "") {
        alert("업체명을 입력하세요.");
        return;
    }

    // AJAX 요청으로 서버에 검색 요청
    $.ajax({
        url: "/product/searchcomname",  // 검색 API URL
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ companyName: companyName }),  // 업체명 전송
        success: function(response) {
            var modalContent = "";

            // 모달이 열리기 전에 항상 테이블 초기화
            $("#searchResults tbody").html("");  

            // 검색 결과가 있을 때만 모달을 띄움
            if (response.length > 0) {
                response.forEach(function(company) {
                    modalContent += `
                        <tr>
                            <td>${company.supplierCode}</td>
                            <td style="text-align: left;">${company.supplierName}</td>
                            <td>${company.businessNumber}</td>
                            <td style="text-align: left;">${company.address}</td>
                            <td><button class="select-company-btn btn btn-info" style="line-height: 10px; text-align: center;" data-id="${company.supplierCode}" data-name="${company.supplierName}">선택</button></td>
                        </tr>`;
                });

                // 검색 결과를 모달창에 표시
                $("#searchResults tbody").html(modalContent);

                // 검색 결과가 있을 때만 모달을 띄움
                $("#searchModal").modal("show");  

                // 선택 버튼 클릭 이벤트 핸들러
                $(".select-company-btn").on("click", function() {
                    var selectedCompanyCode = $(this).data("id");  // 선택한 업체코드
                    var selectedCompanyName = $(this).data("name");  // 선택한 업체명

                    // 선택된 업체코드와 업체명을 해당 input 필드에 삽입
                    $(companyCodeSelector).val(selectedCompanyCode);
                    $(companyNameSelector).val(selectedCompanyName);

                    // 검색 모달 닫기
                    $("#searchModal").modal('hide');

                    // 선택 후 콜백 실행
                    if (callback) {
                        callback();  // 콜백이 있을 경우 실행
                    }
                });
            } else {
                alert("검색 결과가 없습니다.");
                // 검색 결과가 없을 때 모달 닫기 및 테이블 초기화
                $("#searchResults tbody").html("");  // 테이블 내용을 초기화
                $("#searchModal").modal("hide");
            }
        },
        error: function() {
            alert("데이터를 불러오는데 실패했습니다.");
        }
    });
}
function fn_registerProduct(){
	
	var supplier_cd = $("#supplier_cd").val();
	var supplier_nm = $("#supplier_nm").val();
	var product_nm = $("#product_nm").val();
	var product_cd = $("#product_cd").val();
	var purchase_pr = $("#purchase_pr").val().replace(/,/g, '');
	var product_sz = $("#product_sz").val();
	var product_wt = $("#product_wt").val();
	var packaging_unit = $("#packaging_unit").val();
	var safetyinventory_qty = $("#safetyinventory_qty").val();
	var product_log = $("#product_log").val();
	
	var result = confirm("상품을 등록하시겠습니까?");
	
	if(result){
		var productData = {
				supplier_cd : supplier_cd, 
				supplier_nm : supplier_nm, 
				product_nm : product_nm,  
				product_cd  :  product_cd, 
				purchase_pr :  purchase_pr, 
				product_sz  :   product_sz,  
				product_wt   : product_wt, 
				packaging_unit :  packaging_unit, 
				safetyinventory_qty :  safetyinventory_qty, 
				product_log  : product_log, 
             };
	
			$.ajax({
				url : "/product/regproduct",
				type : "post",
				contentType : "application/json",
				data : JSON.stringify(productData),
				success : function(response){
					if(response.success_yn == "success"){
						alert(response.message);
						$('#registerProductModal').modal('hide');
					location.reload();
				} else {
					alert(response.message);
				}
				}
			});
	}
}

$(document).ready(function(){
    // 모달에서 검색 버튼 클릭 시
    $("#registerProductModal #comsearchBtn").on("click", function() {
        fn_searchCompany("#registerProductModal #supplier_cd", "#registerProductModal #supplier_nm", function() {
            // 검색 완료 후 실행될 콜백: 새로운 상품 코드 생성
            generateProductCode();
        });
    });

    function generateProductCode() {
        var supplier_cd = $("#supplier_cd").val(); // 제조사 코드
        console.log(supplier_cd);

        // AJAX 요청을 통해 새로운 상품 코드를 생성
        $.ajax({
            url : "/product/getLastCode",
            type : "post",
            contentType : "application/json",
            data : JSON.stringify({
                 product_cd: $("#product_cd").val(),
       			supplier_cd: $("#supplier_cd").val()
            }),
            success : function(response){
                $("#product_cd").val(response.newcode); // 상품 코드 입력 필드에 새로운 코드 설정
            }
        });
    }
});
