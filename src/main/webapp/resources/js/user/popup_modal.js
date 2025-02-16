
$('#addUserModal').on('hidden.bs.modal', function (e) {
	addUserForm.reset();
	$("[name='user_type']").val('').trigger('change');
	$("#supplier_select").val('').trigger('change');
	$("#vendor_select").val('').trigger('change');
	$("[name='user_lv']").val('').trigger('change');
})

$('#modUserModal').on('hidden.bs.modal', function (e) {
	location.reload();
	$("[name='user_lv']").val('').trigger('change');
})