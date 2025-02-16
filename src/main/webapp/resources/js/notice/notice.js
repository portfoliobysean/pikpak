$(document).ready(function(){
	$("#writeBtn").on("click", function(){
		window.location.href = "/notice/write";
	});
		fn_noticeboardlist(1, 10);
	$(document).on('click', '.page-link', function(e) {
		e.preventDefault();
		var page = $(this).data('page');  // 클릭한 페이지 번호
		fn_noticeboardlist(page, 10);  // page: 클릭한 페이지, size: 10
	});
	 $('#listSize').change(function() {
        var selectedSize = $(this).val();
        fn_noticeboardlist(1, selectedSize);  // 선택된 값에 따라 게시글 목록 불러오기
    });

});
$(document).on("click", "#notice_list tbody tr td#viewpage", function() {
    var nidx = $(this).data('nidx');
    window.location.href = "/notice/" + nidx;  // 게시물 상세보기로 이동
});
$(document).on("mouseenter", "#notice_list tbody tr td#viewpage", function() {
    $(this).css("cursor", "pointer");  // 커서를 손가락 모양으로 변경
});

$(document).on("mouseleave", "#notice_list tbody tr td#viewpage", function() {
    $(this).css("cursor", "default");  // 원래 커서로 복원
});

function fn_noticeboardlist(page, size){
	
	$.ajax({
		url : "/notice/list",
		type : "post",
		contentType : "application/json",
		dataType : "json",
		data : JSON.stringify({
			page : page,size : size
			}),
		success : function(response){
			 fn_renderlist(response.notices);
             renderPagination(response.totalPages, page);
		}
	});
}
    

function fn_renderlist(data){
	
	var info_html = "";
	
	for(var i = 0; i < data.length; i++){
		var date = new Date(data[i].created_dt);
		var formattedDate = date.getFullYear() + '-'
			+ ('0' + (date.getMonth() + 1)).slice(-2) + '-'
			+ ('0' + date.getDate()).slice(-2);
		
		info_html += "<tr>";
		info_html += "<td>" + data[i].nidx + "</td>";
		info_html += "<td id='viewpage' data-nidx='" + data[i].nidx + "' style='text-align:left;'>" + data[i].notice_sb + "</td>";
		info_html += "<td>" + data[i].operator_nm + "</td>";
		info_html += "<td>" + formattedDate + "</td>";
		info_html += "<td>" + data[i].view_count + "</td>";
		info_html += "</tr>";
		
	}
		$("#notice_list tbody").html(info_html);
}
function renderPagination(totalPages, currentPage) {
        var html = "";

        // 이전 페이지 버튼
        if (currentPage > 1) {
            html += '<li class="page-item"><a class="page-link" href="#" data-page="' + (currentPage - 1) + '">이전</a></li>';
        }

        // 페이지 번호 버튼
        for (var i = 1; i <= totalPages; i++) {
            if (i === currentPage) {
                html += '<li class="page-item active"><a class="page-link" href="#" data-page="' + i + '">' + i + '</a></li>';
            } else {
                html += '<li class="page-item"><a class="page-link" href="#" data-page="' + i + '">' + i + '</a></li>';
            }
        }

        // 다음 페이지 버튼
        if (currentPage < totalPages) {
            html += '<li class="page-item"><a class="page-link" href="#" data-page="' + (currentPage + 1) + '">다음</a></li>';
        }

        $("#pagination").html(html);
    }
    
  $(document).ready(function() {
        // HTML에서 operatorLv 값을 가져옴
        var operatorLv = $('#operatorLv').val();
        // operatorLv 값이 1일 때 버튼을 보이도록 설정
        if (operatorLv == 1) {
            $('#writeBtn').css("display", "inline-block");
        }
    });    

    
