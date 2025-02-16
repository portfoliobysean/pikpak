$(document).ready(function(){
    // 페이지 로딩 시 전체 목록을 불러옴
    fn_renderlist();
    
    // 조회 버튼 클릭 시 조회 함수 호출
    $("#searchLogBtn").on("click", function() {
        fn_searchProduct();  // 조회 함수 호출
    });
});

// 전체 목록 또는 검색 결과를 렌더링하는 함수
function fn_renderlist(data = null){
    var url = data ? "/stocklogrecord/search" : "/stocklogrecord/list"; // data가 있으면 검색, 없으면 전체 목록
    var requestData = data ? JSON.stringify(data) : null;

    $.ajax({
        url: url,
        type: "post",
        contentType: "application/json",
        dataType: "json",
        data: requestData,
        success: function(response) {
            var info_html = "";
            
            if (response.length === 0) {
                info_html = "<tr><td colspan='14'>검색 결과가 없습니다.</td></tr>";
            } else {
            
            for (var i = 0; i < response.length; i++) {
                var logDate = new Date(response[i].log_dt);  // log_dt를 Date 객체로 변환

                // 연-월-일 시:분:초 포맷
                var formattedDate = logDate.getFullYear() + '-' + 
                    ('0' + (logDate.getMonth() + 1)).slice(-2) + '-' + 
                    ('0' + logDate.getDate()).slice(-2) + ' ' + 
                    ('0' + logDate.getHours()).slice(-2) + ':' + 
                    ('0' + logDate.getMinutes()).slice(-2) + ':' + 
                    ('0' + logDate.getSeconds()).slice(-2);

                info_html += "<tr>";
                info_html += "<td>" + (i + 1) + "</td>";
                info_html += "<td>" + (response[i].product_cd || '-') + "</td>";
                info_html += "<td style='text-align:left;'>" + (response[i].product_nm || '-') + "</td>";
                info_html += "<td>" + (response[i].location_cd || '-') + "</td>";
                info_html += "<td>" + (response[i].before_qty || '-') + "</td>";
                info_html += "<td>" + (response[i].after_qty || '-') + "</td>";
                info_html += "<td>" + (response[i].changed_qty || '-') + "</td>";
                info_html += "<td>" + (response[i].before_location_cd || '-') + "</td>";
                info_html += "<td>" + (response[i].after_location_cd || '-') + "</td>";
                info_html += "<td style='text-align:left;'>" + (response[i].before_product_nm || '-') + "</td>";
                info_html += "<td style='text-align:left;'>" + (response[i].after_product_nm || '-') + "</td>";
                info_html += "<td style='text-align:left;'>" + (response[i].action_type || '-') + "</td>";
                info_html += "<td style='text-align:left;'>" + (response[i].dispose_reason || '-') + "</td>";
                info_html += "<td>" + formattedDate + "</td>"; 
                info_html += "<td>" + (response[i].operator_nm || '-') + "</td>";
                info_html += "<td style='text-align:left;'>" + (response[i].notes || '-') + "</td>";
                info_html += "</tr>";
            }
            }
            // 테이블에 HTML 출력
            $("#stocklog_list tbody").html(info_html);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error("데이터 불러오기 실패: ", textStatus, errorThrown);
        }
    });
}

// 검색 조건에 따라 데이터를 필터링하여 렌더링하는 함수
function fn_searchProduct() {
    var fromDate = $("#fromDate").val() || null;
    var toDate = $("#toDate").val() || null;
    var productType = $("#LogTypeFilter").val();
    var searchKeyword = $("#searchkeyword").val() || null;

    // 검색 조건을 위한 데이터 객체
    var searchData = {
        fromDate: fromDate ? fromDate : null,
        toDate: toDate ? toDate : null,
        productType: productType || 'all',
        searchKeyword: searchKeyword || '',
    };

    // 검색 데이터를 기반으로 목록을 렌더링
    fn_renderlist(searchData);
}
