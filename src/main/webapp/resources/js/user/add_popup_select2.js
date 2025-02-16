//select2 검색기능
$("[name='user_type']").select2({
	theme: 'bootstrap',
	dropdownParent : $('#addUserModal'),
	placeholder : '유형 선택',
	minimumResultsForSearch: -1,
	//closeOnSelect : true
	//dropdownAutoWidth : true
	allowClear : true
});

$("#supplier_select").select2({
	theme: 'bootstrap',
	dropdownParent : $('#addUserModal'),
	placeholder : '회사 검색',
	//closeOnSelect : true
	//dropdownAutoWidth : true
	allowClear : true
});

$('#supplier_select').on('change',function() {
	var selectedOption =  $(this).find('option:selected');
	let cdValue = selectedOption.data('trader-cd');
	$('input[name="company_cd"]').val(cdValue);
});

$("#vendor_select").select2({
	theme: 'bootstrap',
	dropdownParent : $('#addUserModal'),
	placeholder : '회사 검색',
	//closeOnSelect : true
	//dropdownAutoWidth : true
	allowClear : true
});

$('#vendor_select').on('change',function() {
	var selectedOption =  $(this).find('option:selected');
	let cdValue = selectedOption.data('trader-cd');
	$('input[name="company_cd"]').val(cdValue);
});


$("[name='user_lv']").select2({
	theme: 'bootstrap',
	dropdownParent : $('#addUserModal'),
	placeholder : '등급 선택',
	minimumResultsForSearch: -1,
	//closeOnSelect : true
	//dropdownAutoWidth : true
	allowClear : true
});