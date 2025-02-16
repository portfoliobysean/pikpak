$("[name='user_lv']").select2({
	theme: 'bootstrap',
	dropdownParent : $('#modUserModal'),
	placeholder : '등급 선택',
	minimumResultsForSearch: -1,
	//closeOnSelect : true
	//dropdownAutoWidth : true
	allowClear : true
});